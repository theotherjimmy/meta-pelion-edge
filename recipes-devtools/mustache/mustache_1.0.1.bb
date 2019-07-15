DESCRIPTION = "An implementation of the mustache template language in Go"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=c85970e6b602135bbaf9ca00d27b7149"

inherit go pkgconfig

PR = "r1"
SRC_URI = "git://git@github.com/cbroglie/mustache.git;protocol=ssh;rev=eb931a9d20042e51d8a3a9ad5a9ba9bc8282c564"

RDEPENDS_mustach-dev = "bash"

GO_IMPORT = "github.com/cbroglie/mustache/"
FILES_${PN} = "/usr/bin/mustache"

do_install() {
  install -d ${D}${bindir}
  install -m 0755 ${B}/${GO_BUILD_BINDIR}/* ${D}${bindir}/
}
