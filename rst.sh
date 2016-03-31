writeSubstitution() {
  local name="${1}"
  local value="${2}"

  echo ".. |${name}| replace:: ${value}"
}

writeCommonSubstitutions() {
  writeSubstitution "package version" "${versionNumber}"
  writeSubstitution "source revision" "${sourceRevision}"
  writeSubstitution "build time" "${buildDate} ${buildTime} ${buildZone}"
}

