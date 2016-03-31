set -e

relativePath() {
  local path="${1}"

  echo "$(realpath --relative-to=. "$(dirname "${path}")")"
}

programDirectory="$(relativePath "${0}")"

runApplicationScript() {
  local directory="."

  while true
  do
    local script="${directory}/application.sh"
    [ ! -f "${script}" ] || break
    directory="${directory}/.."
  done

  . "${script}"
}
runApplicationScript

