# 💳 ML-Powered Mobile Fraud Detection App

This project presents a **mobile-integrated machine learning solution** for **real-time credit card fraud detection**, combining the power of artificial intelligence with the accessibility of Android applications. Designed as a final-year Bachelor's project, it demonstrates a scalable prototype that detects fraudulent credit card transactions using advanced classification algorithms.

---

## 🚀 Project Overview

With the rise of digital banking and e-commerce, **credit card fraud** has become a pressing issue. This project proposes a secure, intelligent, and easy-to-use mobile app that leverages **machine learning algorithms** to flag suspicious transactions and empower users to act on potential fraud.

---

## 📱 Features

- 🔐 **User Authentication** with Firebase
- 📊 **Real-time Fraud Prediction** using trained ML models
- 🧠 **ML Algorithms**: Logistic Regression, Decision Tree, Random Forest
- ⚖️ **Imbalanced Data Handling**: SMOTE, Over & Under Sampling
- 📈 Achieved **96% Accuracy** on synthetic dataset
- ☁️ **Cloud Backend**: Firebase Realtime Database
- 🌐 **Flask API hosted on Heroku** to serve predictions to the mobile app
- 🔍 **Transaction History View** for users

---

## 🧠 Machine Learning Workflow

1. **Data Source**: [Kaggle Credit Card Fraud Dataset](https://www.kaggle.com/datasets/mlg-ulb/creditcardfraud)
2. **Preprocessing**: Cleaning, Normalization, Feature Selection
3. **Handling Imbalance**: Applied SMOTE, ROS, RUS
4. **Models Trained**:
   - Logistic Regression
   - Decision Tree
   - Random Forest with Hyperparameter Tuning (GridSearchCV)
5. **Evaluation Metrics**: Accuracy, Precision, Recall, F1-Score

> 📌 Best performing model: **Random Forest + SMOTE** with 96% Accuracy

---

## 🛠 Tech Stack

| Component        | Technology                |
|------------------|---------------------------|
| ML Model         | Python, Scikit-learn      |
| Notebook         | Jupyter Notebook          |
| API Server       | Flask, Heroku             |
| Mobile App       | Android Studio (Java)     |
| Backend Database | Firebase Realtime Database|
| Auth             | Firebase Authentication   |
| Hosting          | GitHub, Heroku            |

---

## 🔒 Limitations & Future Enhancements

- Currently uses **synthetic data** for simulation, not live data.
- Fraud **alerts are not yet implemented** (to be added using Firebase Cloud Messaging).
- No live feedback loop yet from user to model.
- Future updates will include:
  - Real-time transaction monitoring from bank APIs
  - Push notifications for detected fraud
  - User feedback integration for continuous learning
