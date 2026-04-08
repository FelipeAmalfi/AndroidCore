# How Agents Work

## Purpose

The repository includes agent descriptors so feature work can be split into focused execution units.

This guide explains the idea without repeating the detailed rules already stored in `.github/skills/` and `.github/agents/`.

## The separation of responsibilities

### Docs

`docs/` exists to help humans understand the system.

### Skills

`.github/skills/` contains the reusable standards that agents and maintainers follow.

Examples include:

- workflow order
- clean architecture rules
- MVI rules
- naming conventions
- mapping constraints

### Agents

`.github/agents/` contains the execution units that apply those skills to concrete tasks.

Each agent stays lightweight by referencing the skills it needs instead of embedding every rule directly.

## Current agent set

The repository currently includes these agent descriptors:

- `data-layer.agent.md`
- `domain.agent.md`
- `mapper.agent.md`
- `presentation.agent.md`
- `test.agent.md`
- `enhancement.agent.md`
- `documentation.agent.md`

## What each agent is for

| Agent | Main responsibility |
|---|---|
| data layer | data access artifacts and repository implementation |
| domain | domain models, contracts, and use cases |
| mapper | explicit extension-based transformations |
| presentation | MVI contracts and ViewModel orchestration |
| test | validation across layers and scenarios |
| enhancement | optional resilience and performance improvements |
| documentation | maintenance of modular knowledge and documentation boundaries |

## How agents stay consistent

Agents are not the source of truth for every rule.
They remain consistent because they compose shared skills.

For example:

- presentation relies on MVI, naming, mapping, and core-usage skills
- data relies on architecture, error-handling, workflow, and core-usage skills
- documentation relies on governance and architecture skills

That arrangement reduces duplication and makes the system easier to evolve.

## Stable invocation idea

Agent descriptors include stable identifiers such as `data-layer-agent` or `presentation-agent`.

That matters because file locations and filenames may evolve, while the agent identity should remain stable for tooling and prompts.

## Human workflow vs agent workflow

From a human perspective, the process is simple:

1. understand the feature
2. move through the layers in the expected order
3. validate at each stage
4. keep responsibilities separate

From the agent perspective, the same flow becomes explicit handoffs between specialized executors.

## When to read the agent files directly

Go to `.github/agents/*.agent.md` when you need:

- exact agent responsibility
- expected inputs and outputs
- referenced skills
- execution-focused instructions

Go to `.github/skills/*.skill.md` when you need:

- the actual standards behind those instructions

## What this guide intentionally avoids

This file does not duplicate:

- naming tables
- template code
- validation checklists
- phase-by-phase implementation rules

Those are better maintained in `.github/skills/` and `.github/agents/`.

## Suggested follow-up reading

- `docs/guides/how-to-create-feature.md`
- `docs/examples/end-to-end-example.md`
- `.github/agents/presentation.agent.md`
- `.github/skills/agent-workflow.skill.md`

