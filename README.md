<div align="center">
   <img src="./.github/logo.svg" alt="logo" width="35%">
   <h1>Code Output Quiz</h1>

   <div>
      <a href="https://github.com/krivolapovdev/code-output-quiz/blob/master/LICENSE">
         <img src="https://img.shields.io/badge/license-MIT-blue.svg" alt="MIT License">
      </a>
      <a href="https://github.com/krivolapovdev/code-output-quiz">
         <img src="https://img.shields.io/github/repo-size/krivolapovdev/code-output-quiz.svg" alt="Repository Size">
      </a>
      <a href="https://github.com/krivolapovdev/code-output-quiz/pulls">
         <img src="https://img.shields.io/github/issues-pr-raw/krivolapovdev/code-output-quiz.svg" alt="Pull Requests">
      </a>
      <a href="https://github.com/krivolapovdev/code-output-quiz/issues">
         <img src="https://img.shields.io/github/issues-raw/krivolapovdev/code-output-quiz.svg" alt="Open Issues">
      </a>
      <a href="https://github.com/krivolapovdev/code-output-quiz/graphs/commit-activity">
         <img src="https://img.shields.io/github/last-commit/krivolapovdev/code-output-quiz.svg" alt="Last Commit">
      </a>
      <a href="https://github.com/krivolapovdev/code-output-quiz/graphs/commit-activity">
         <img src="https://github.com/krivolapovdev/code-output-quiz/actions/workflows/main.yml/badge.svg" alt="CI/CD">
      </a>
   </div>

  <p>Made with ❤️</p>
</div>

## 📚 Table of Contents

- [📑 Introduction](#-introduction)
- [🌐 Site](#-site)
- [🧠 Features](#-features)
- [🚀 Quickstart](#-quickstart)
- [🛠 TechStack](#-techstack)
- [🧑‍💻 Contributing](#-contributing)
- [💬 Support](#-support)
- [💖 Like](#-like)
- [📬 Contact](#-contact)
- [⚖️ License](#️-license)

## 📑 Introduction

**Code Output Quiz** is an open-source platform where users test their programming knowledge by
predicting the output of code snippets.

Level up your coding skills with unique, auto-generated multiple-choice questions designed to
challenge how well you understand real-world behavior of code.

Choose your programming language, set your difficulty level, and start solving!

## 🌐 Site

<div align="center">
    <a href="https://code-output-quiz.ru/">
        <img src="./.github/site.gif" alt="Website Preview" width="80%" style="border-radius: 6px;">
    </a>
    <br />
    <a href="https://code-output-quiz.ru/" target="_blank" rel="noopener noreferrer">
        ➡️ Visit Live Site
    </a>
</div>

## 🧠 Features

- 🧬 AI-powered quiz generation.
- 🌐 Language & difficulty filters.
- 🔒 Secure authentication using JWT.
- 📈 Real-time observability (Prometheus, Grafana, Loki, Zipkin).
- 📦 Microservice architecture.
- 🚪 API Gateway for unified routing and access control.
- 📨 Kafka-powered event-driven communication.
- 🖥️ Responsive UI built with React & Tailwind CSS.
- ⚙️ DevOps-ready setup with Docker & GitHub Actions.

## 📦 Quickstart

Want to run the project locally?

Prerequisites:

- [Docker](https://www.docker.com/)
- [Git](https://git-scm.com/)

> [!NOTE]
> The project is in active development. Expect frequent changes.

1. **Clone** this repository:
   ```bash
   git clone https://github.com/krivolapovdev/code-output-quiz
   cd code-output-quiz
   ```
2. Configure **environment variables**:
   ```bash
   cp .env.example .env
   nano .env
   ```
3. **Build and start** all services using Docker:
   ```bash
   docker buildx bake -f docker-bake.hcl --load
   docker compose -f docker-compose.yml up
   ```
4. Open **http://localhost** in your browser.

## 🛠 TechStack

| 🧩 Area           | 🧰 Technologies                                                                               |
|-------------------|-----------------------------------------------------------------------------------------------|
| **Backend**       | Spring Boot (WebFlux), PostgreSQL (R2DBC), Kafka, Redis, Eureka Server, Config Server, Flyway |
| **Frontend**      | React, Vite, Tailwind CSS, Zustand, Axios, Prism.js                                           |
| **Observability** | Prometheus, Grafana, Loki, Zipkin                                                             |
| **Gateway**       | Spring Cloud Gateway, NGINX                                                                   |
| **DevOps**        | Docker, GitHub Actions                                                                        |

## 🧑‍💻 Contributing

We welcome all contributions — code, documentation, ideas, and feedback!

If you'd like to help:

1. 🍴 Fork the repository
2. 📦 Create a new branch: `git checkout -b feature/my-feature`
3. 🛠️ Make your changes
4. ✅ Commit with a clear message
5. 📬 Open a pull request describing what you’ve done

You can contribute by:

- 🎨 Improving the UI or UX
- 🐞 Fixing bugs or improving performance
- 🧩 Adding new quiz features or languages
- 📝 Enhancing documentation or developer setup
- 📈 Improving observability or CI/CD workflows

## 💬 Support

Encountering a bug? Have a question? Want to suggest a feature? We're here to help!

If you run into any problems while using the app or setting up the project, please don't hesitate
to [open an issue](https://github.com/krivolapovdev/code-output-quiz/issues/new) on GitHub.

> [!TIP]
> The more details you provide — logs, screenshots, steps to reproduce — the faster we can help.

You can also check the
existing [issues list](https://github.com/krivolapovdev/code-output-quiz/issues) to see if your
question has already been answered.

## 💖 Like

If you find this project useful or interesting, please consider starring ✨ it on GitHub!

It helps others discover the project and motivates continued development.

## 📬 Contact

| 🌐 Platform      | 🔗 Link                                                                       |
|------------------|-------------------------------------------------------------------------------|
| 📧 Email         | [KrivolapovVladislav1998@gmail.com](mailto:KrivolapovVladislav1998@gmail.com) |
| 💬 Telegram      | [@krivolapovdev](https://t.me/krivolapovdev)                                  |
| :octocat: GitHub | [@krivolapovdev](https://github.com/krivolapovdev)                            |

## ⚖️ License

This project is licensed under
the [MIT License](https://github.com/krivolapovdev/krivolapovdev.github.io/blob/main/LICENSE).
