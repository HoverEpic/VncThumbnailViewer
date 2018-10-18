#!/bin/bash

# Vino script, enable remote view and control

# Vino password, if changed, please run this script again on all servers.
# -> VNC Enc. Password
VINO_PASSWORD='65scv52n'

# enable vino
gsettings set org.gnome.Vino enabled true

# set password
gsettings set org.gnome.Vino vnc-password $VINO_PASSWORD

# no notify on connect
gsettings set org.gnome.Vino notify-on-connect false

# not ask for connection
gsettings set org.gnome.Vino prompt-enabled false

# not display icon
gsettings set org.gnome.Vino icon-visibility never

# disable protocol encryption
gsettings set org.gnome.Vino require-encryption false

# copy daemon
sudo sh -c 'cp target/VncThumbnailViewer-1.0-jar-with-dependencies.jar /bin'

sudo sh -c 'cat > /etc/init/vncdaemon.conf <<EOF

#
# This task is run on startup to start the VncThumbnailViewer daemon

description     "set system hostname"

start on startup

task
exec java -jar /bin/VncThumbnailViewer-1.0-jar-with-dependencies.jar daemon true

EOF'