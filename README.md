# 🗂️ Draft Projects

A collection of code — interview assignments, tutorial follow-alongs, and ideas still taking shape. Nothing here is meant to be production-grade; it's a playground for learning, practicing, and testing things out.

> ⚠️ Each project lives in its own folder and is meant to be run in its own environment. Check individual folders for extra setup notes where available.

## 📑 Table of Contents

- [🎯 Interview Projects](#-interview-projects)
- [📘 Tutorial-Inspired Projects (roadmap.sh)](#-tutorial-inspired-projects-roadmapsh)
- [🥾 Hiking & Location Projects](#-hiking--location-projects)
- [🧪 Other Practice & Draft Projects](#-other-practice--draft-projects)
- [🛠️ Utilities & Setup](#️-utilities--setup)
- [🤝 Adding Existing Projects](#-adding-existing-projects)

## 🎯 Interview Projects

- **LiveFootballWorldCup-java** ⚽ — A Java library that manages a live football scoreboard: start a match at 0-0, update scores by team name, get a summary ordered by total score, and finish (remove) a match. Built from an explicit interview-style spec, with assumptions documented in its README.
- **LiveFootballWorldCup-kotlin** ⚽ — The same scoreboard exercise, implemented in Kotlin with its own set of documented assumptions (e.g. how "matcher" objects are used to update or finish a match).
- **doctor-file-processor** 🩺 — A Spring Boot service built for a take-home assignment: it periodically scans a directory for JSON documents, validates them, and moves them to `valid/` or `invalid/` with an audit log. It also exposes an HTTP endpoint for on-demand validation with a custom-built rate limiter (5 requests/minute, no third-party library), tested under concurrent load.

## 📘 Tutorial-Inspired Projects (roadmap.sh)

- **pybroadcast** 📡 — A Python WebSocket broadcast server, packaged as a CLI tool (`broadcast-server`), that lets clients connect and receive messages broadcast by the server. Based on the [roadmap.sh Broadcast Server project](https://roadmap.sh/projects/broadcast-server).
- **ImageProcessingService** 🖼️ — A Python microservice demonstrating image handling with Kafka messaging, containerized dependencies, and a logging pipeline (Filebeat → Logstash → OpenSearch). Its README also outlines a planned AWS deployment (API Gateway, Lambda, Aurora, S3). Related to the [roadmap.sh Image Processing Service project](https://roadmap.sh/projects/image-processing-service).
- **linux-setup** 🐧 — Detailed, hands-on notes from working through the [roadmap.sh Linux Server Setup project](https://roadmap.sh/projects/linux-server-setup): creating a sudo user, hardening SSH, configuring UFW, enabling unattended upgrades, setting up Fail2Ban, and inspecting logs with `journalctl`.

## 🥾 Hiking & Location Projects

Several related attempts at combining hike-tracking data (GPX files, Strava) with photos.

- **HelloWorld** 👋 — Despite the name, this is a C# .NET project that parses GPX files and pulls trail data from Strava through a `StravaClient` — an early experiment that looks like a precursor to `GpxClient`/`HikingClient`.
- **GpxClient** 🧭 — A Python/FastAPI service that parses uploaded GPX files into coordinate points and integrates with the Strava API (OAuth login, token caching via MongoDB, fetching activities).
- **HikingClient** 🥾 — A C# project whose README documents research into matching hike GPX data with photos: investigating the Strava API (OAuth 2.0, activity endpoints) and the Microsoft Graph API for pulling images from OneDrive.
- **HikingClient-py** 🥾 — The Python/FastAPI counterpart: includes a Microsoft Graph/OneDrive OAuth login flow (`router.py`) and a local file-processing entry point (`main.py`).
- **photo-map** 📷 — An early-stage FastAPI scaffold (its app title is still "GPX Client") that renders a template via a Wikipedia-client auth provider — a work-in-progress take on combining photos with location data.
- **HikingPinpoints** 📍 — Currently just a project skeleton (IDE config only, no source code yet).
- **hike-map** 🗺️ — Also just a project skeleton at this point (IDE config only, no source code yet).

## 🧪 Other Practice & Draft Projects

- **aws-python** ☁️ — A sample Python app deployed to AWS Lambda with DynamoDB, using CDK/SAM for deployment and a GitHub Actions CD pipeline. Its README also covers IAM permissions, VPC networking, and CloudTrail logging.
- **cli_project** ⌨️ — "MCP Chat," a command-line chat client for the Anthropic API that supports document retrieval (`@doc`) and slash commands, built around the Model Context Protocol (MCP).
- **call_antropic_api** 🤖 — A Jupyter notebook experimenting with direct calls to the Anthropic API, with small helper functions for building a message history.
- **claude** 🧠 — Claude Code configuration: a `pr-description` skill that writes pull-request descriptions from `git log`, and an accompanying agent file for more detailed PR instructions.
- **fast-api** ⚡ — A FastAPI practice project with a basic item/project CRUD API, custom logging and timing middleware, and unit tests.
- **oauth-py** 🔐 — A FastAPI app demonstrating Google's OAuth 2.0 authorization code flow (via the People API), with OAuth wired directly into the Swagger UI.
- **image-sharing** 🖼️ — A FastAPI app with user authentication (via `fastapi-users`) and image upload support (using ImageKit), modeling users and posts.
- **postgredatabase** 🐘 — A Spring Boot + PostgreSQL/JPA app modeling authors and books, with repository, service, and controller layers plus integration tests.
- **database** 🗄️ — A minimal Spring Boot skeleton with a JDBC starter dependency — set up, but without business logic added yet.
- **database-backup** 💾 — A placeholder Python script intended for automating database backups (currently empty).
- **py-salesforce-sync** 🔄 — A placeholder for a Python service to sync data with Salesforce (currently just a stub).
- **mobile** 📱 — A small Python script that uses `adb` to send an SMS from a connected Android device.
- **static-content** 📄 — A simple static site (sample "fruits" and "vegetables" pages) served via an Nginx config — practice with static content hosting.
- **store** 🛒 — A Spring Boot + MongoDB app modeling a store: software-engineer listings plus order and payment services (Stripe and PayPal integrations).

## 🛠️ Utilities & Setup

- **.idea** — IntelliJ IDEA project configuration files.
- **LICENSE** — MIT License covering this repository.

## 🤝 Adding Existing Projects

Since each project originally lived in its own local repository, they were merged into this monorepo using local remotes and unrelated history merges:

```bash
git remote add football-java ~/Projekti/LiveFootballWorldCup-java/
git fetch

git checkout -b football-java --track football-java/master

git merge football-java --allow-unrelated-histories
```

---

Got a project idea you want turned into its own thing? Feel free to fork it out into a standalone repo once it's ready. 🚀