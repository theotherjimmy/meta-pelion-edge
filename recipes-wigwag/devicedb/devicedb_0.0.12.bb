DESCRIPTION = "devicedb distributed database"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=58b1e0eba1968eab8a0f46444674102a"
# avoid the `-linkshared` option in this recipe as it causes a panic
GO_LINKSHARED=""

inherit go pkgconfig gitpkgv systemd

PR = "r5"
SRC_URI = "git://git@github.com/armPelionEdge/devicedb.git;protocol=ssh;branch=master;name=ddb \
file://devicedb \
file://devicedb.service \
"

SYSTEMD_PACKAGES = "${PN}"
SYSTEMD_SERVICES_${PN} = "devicedb.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

SRCREV_FORMAT = "ddb"
SRCREV_ddb = "${AUTOREV}"
GO_IMPORT = "github.com/armPelionEdge/devicedb/"

DEPENDS = ""
RDEPENDS_${PN} += " logrotate"

FILES_${PN} = "\
  /wigwag/system/bin/devicedb\
  /etc/logrotate.d/devicedb\
  ${systemd_system_unitdir}/devicedb.service\
  "
do_install() {
  install -d "${D}/wigwag/system/bin"
  install -m 0755 "${B}/${GO_BUILD_BINDIR}/devicedb" "${D}/wigwag/system/bin"
  install -d "${D}${sysconfdir}/logrotate.d/"
  install -m 644 "${WORKDIR}/devicedb" "${D}${sysconfdir}/logrotate.d"
  install -d ${D}${systemd_system_unitdir}
  install -m 644 ${WORKDIR}/devicedb.service ${D}${systemd_system_unitdir}
}

