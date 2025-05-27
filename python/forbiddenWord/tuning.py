import pandas as pd

# 1단계: train.tsv 데이터 불러오기
# TSV 파일 로딩
df = pd.read_csv("train.tsv", sep="\t")
# df = pd.read_csv("train.tsv", sep="\t").head(1000)

# 컬럼명 통일 (KoElectra 튜닝용)
df = df.rename(columns={"text": "sentence", "label": "label"})

# 확인
print(df.head())
print(df['label'].value_counts())

# 2단계: HuggingFace Dataset으로 변환
from datasets import Dataset

# Pandas → HuggingFace Dataset 변환
dataset = Dataset.from_pandas(df)

# 3단계: 토크나이징 + 모델 불러오기
from transformers import ElectraTokenizer, ElectraForSequenceClassification

MODEL_NAME = "monologg/koelectra-base-discriminator"

tokenizer = ElectraTokenizer.from_pretrained(MODEL_NAME)

def tokenize(batch):
    return tokenizer(batch["sentence"], padding="max_length", truncation=True)

tokenized_dataset = dataset.map(tokenize)

model = ElectraForSequenceClassification.from_pretrained(
    MODEL_NAME,
    num_labels=2
)

# 4단계: Trainer로 학습
from transformers import Trainer, TrainingArguments

training_args = TrainingArguments(
    output_dir="./results",
    eval_strategy="epoch",         # ✅ 올바른 이름
    save_strategy="epoch",               # ✅ 각 epoch 끝날 때 체크포인트 저장
    save_total_limit=2,                  # ✅ 최근 2개만 유지
    load_best_model_at_end=True,         # ✅ 가장 성능 좋은 모델 자동 로드
    per_device_train_batch_size=16,
    per_device_eval_batch_size=16,
    num_train_epochs=3,
    logging_dir="./logs",
    logging_steps=10,
)


trainer = Trainer(
    model=model,
    args=training_args,
    train_dataset=tokenized_dataset,
    eval_dataset=tokenized_dataset,
)

trainer.train()

# 5단계: 모델 저장
trainer.save_model("finetuned-koelectra-badword")
