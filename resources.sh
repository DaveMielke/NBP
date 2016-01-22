xmlVersion="1.0"
xmlEncoding="utf-8"

printResourcesPrologue() {
  echo "<?xml version=\"${xmlVersion}\" encoding=\"${xmlEncoding}\" ?>"
  echo ""
  echo "<resources>"
}

printResourcesEpilogue() {
  echo "</resources>"
}

printSimpleResource() {
  local type="${1}"
  local name="${2}"
  local value="${3}"

  echo "  <${type} name=\"${name}\">${value}</${type}>"
}

printArrayResource() {
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
