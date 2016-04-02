set -e

relativePath() {
  local path="${1}"

  echo "$(realpath --relative-to=. "$(dirname "${path}")")"
}

programDirectory="$(relativePath "${0}")"
programName="${0##*/}"

programMessage() {
  local message="${1}"

  echo >&2 "${programName}: ${message}"
}

semanticError() {
  local message="${1}"

  programMessage "${message}"
  exit 3
}

runApplicationScript() {
  local script="application.sh"
  local directory="."

  while true
  do
    local path="${directory}/${script}"
    [ ! -f "${path}" ] || break

    [ ! -d "${directory}/.git" ] || semanticError "application script not found"
    directory="${directory}/.."
  done

  . "${path}"
}
runApplicationScript

