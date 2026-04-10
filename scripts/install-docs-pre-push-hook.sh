#!/usr/bin/env sh
set -eu

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
TEMPLATE_PATH="$REPO_ROOT/scripts/hooks/pre-push"
HOOK_PATH="$REPO_ROOT/.git/hooks/pre-push"

if [ ! -f "$TEMPLATE_PATH" ]; then
  echo "Hook template not found: $TEMPLATE_PATH"
  exit 1
fi

cp "$TEMPLATE_PATH" "$HOOK_PATH"
chmod +x "$HOOK_PATH"

echo "Installed docs pre-push hook: $HOOK_PATH"

