# AndroidCore Local MCP Server

This folder contains a local-only Node.js server that exposes agents and skills exported into `.ai/`.

## Endpoints

- `GET /health`
- `GET /agents`
- `GET /agents/:name`
- `POST /generate`

## Local run

```powershell
npm install
npm run start
```

The server starts at `http://127.0.0.1:3333`.

