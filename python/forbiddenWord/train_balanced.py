import pandas as pd
from datasets import Dataset
from transformers import ElectraTokenizer, ElectraForSequenceClassification
from transformers import Trainer, TrainingArguments

# 1단계: 데이터 로딩
df = pd.read_csv("train.tsv", sep="\t")
df = df.rename(columns={"text": "sentence", "label": "label"})

# ✅ 라벨 1 (금칙어) 500개, 라벨 0 (정상) 500개 → 총 1000개
label_1 = df[df['label'] == 1].sample(n=500, random_state=42)
label_0 = df[df['label'] == 0].sample(n=500, random_state=42)
balanced_df = pd.concat([label_0, label_1]).sample(frac=1, random_state=42).reset_index(drop=True)

# 확인용 출력
print(balanced_df['label'].value_counts())
print(balanced_df.head())

# 2단계: HuggingFace Dataset으로 변환
dataset = Dataset.from_pandas(balanced_df)

# 3단계: KoElectra 모델 및 토크나이저 불러오기
MODEL_NAME = "monologg/koelectra-base-discriminator"
tokenizer = ElectraTokenizer.from_pretrained(MODEL_NAME)

def tokenize(batch):
    return tokenizer(batch["sentence"], padding="max_length", truncation=True)

tokenized_dataset = dataset.map(tokenize)

model = ElectraForSequenceClassification.from_pretrained(
    MODEL_NAME,
    num_labels=2
)

# 4단계: Trainer 설정 및 학습
training_args = TrainingArguments(
    output_dir="./results",
    eval_strategy="epoch",
    save_strategy="epoch",
    save_total_limit=2,
    load_best_model_at_end=True,
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
