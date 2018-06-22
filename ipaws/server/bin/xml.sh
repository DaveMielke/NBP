readonly xpathAlert="/ns1:alerts/alert"

xpathGet() {
   local file="${1}"
   local xpath="${2}"

   xpath -q -e "${xpath}" "${file}"
}

ipawsGetAlertCount() {
   local file="${1}"

   xpathGet "${file}" "count(${xpathAlert})"
}

ipawsGetAlertElement() {
   local file="${1}"
   local index="${2}"

   xpathGet "${file}" "${xpathAlert}[${index}]"
}

