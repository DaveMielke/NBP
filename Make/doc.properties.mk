BUILD_PROPERTIES = properties.rst
LOCAL_FILES += $(BUILD_PROPERTIES)
properties: $(BUILD_PROPERTIES)
$(BUILD_PROPERTIES):
	$(TOP)doc.mkproperties >$@

