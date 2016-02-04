xmlVersion="1.0"
xmlEncoding="utf-8"

writeResourcesPrologue() {
  echo "<?xml version=\"${xmlVersion}\" encoding=\"${xmlEncoding}\" ?>"
  echo ""
  echo "<resources>"
}

writeResourcesEpilogue() {
  echo "</resources>"
}

writeSimpleResource() {
  local type="${1}"
  local name="${2}"
  local value="${3}"

  echo "  <${type} name=\"${name}\">${value}</${type}>"
}

writeArrayResource() {
  local type="${1}"
  local name="${2}"
  shift 2

  local element="${type}-array"
  echo "  <${element} name=\"${name}\">"

  local value
  for value
  do
    echo "    <item>${value}</item>"
  done

  echo "  </${element}>"
}

writeCommonResources() {
  writeSimpleResource integer "${applicationPrefix}_version_code" "${versionCode}"
  writeSimpleResource string "${applicationPrefix}_version_name" "${versionNumber}"
  writeSimpleResource string "${applicationPrefix}_source_revision" "${sourceRevision}"
  writeSimpleResource string "${applicationPrefix}_build_time" "${buildDate}@${buildTime} ${buildZone}"
}

