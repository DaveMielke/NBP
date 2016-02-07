writeSubstitution() {
  local name="${1}"
  local value="${2}"

  echo ".. |${name}| replace:: ${value}"
}

