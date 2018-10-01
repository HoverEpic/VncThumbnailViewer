#!/bin/bash

//Vino script, enable remote view and control

// enable vino
gsettings set org.gnome.Vino enable true

// set password
gsettings set org.gnome.Vino vnc-password 65scv52n

// no notify on connect
gsettings set org.gnome.Vino notify-on-connect false

// not ask for connection
gsettings set org.gnome.Vino prompt-enabled false

// disable protocol encryption
gsettings set org.gnome.Vino require-encryption false
