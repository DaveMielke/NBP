acquireExclusiveLock() {
   local name="${1}"

   exec 9>>"${dataDirectory}/${name}.lock"
   logInfo "acquiring lock: ${name}"
   flock --nonblock --exclusive 9
   logInfo "lock acquired: ${name}"
} && readonly -f acquireExclusiveLock

