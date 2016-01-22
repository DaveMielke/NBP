xmlVersion="1.0"
xmlEncoding="utf-8"

printResourcesHeader() {
  echo "<?xml version=\"${xmlVersion}\" encoding=\"${xmlEncoding}\" ?>"
  echo ""
  echo "<resources>"
}

printResourcesFooter() {
  echo "</resources>"
}

printResource() {
  local type="${1}"
  local name="${2}"
  local value="${3}"

  echo "  <${type} name=\"${name}\">${value}</${type}>"
}
