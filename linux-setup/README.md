# Linux server setup

Inspired by [roadmap.sh project](https://roadmap.sh/projects/linux-server-setup)

## Prerequsites

Instructions originally specify that we should use VPS. However you can also do this on Linux docker container or WLS. The following instructions assume we are using Docker.

First, we need to install Docker

Run docker container Linux Alpine:


    docker run -p 22:22 -d --name alpine nginx:alpine

    docker run -p 22:22 -td --name ubuntu ubuntu

After creating it, ssh into container:

    docker exec -it [container-id] /bin/sh

Or execute a specific command:

   docker exec -it [container-id] [command]

## Requirements

You are required to perform the following tasks on a fresh Ubuntu server:

- User Setup: Create a non-root user with sudo privileges. This user should be used for all future server administration instead of root.

*Notes*

Creating an user is done using the following command:

    adduser [USERNAME] // Alpine

    useradd -r [USERNAME] // Ubuntu


We can then change the users pasword using this command:

    passwd john

This command will prompt us for the new password. It will also issue a warning if the password is bad - too similar to username.


We can add or remove users sudo permisisons by adding or removing it from the "sudo" group:

    usermod -a -G nonsudo john

    or

    addgroup [username] [group] // Alpine

    or

    groupadd -U [USER] [GROUP]

It is not recommended to add sudo permissions to another user. In such cases, its better to create a new group for users:

    groupadd [GROUP]

    or

    addgroup [group-name]

You can verify that these were added by running:


    getent passwd // displays active users and their home dirs
    getent group // displays groups and their members

The same information is also available in `/etc/passwd` and `/etc/group`. When printing this content, you can filter out results by username or group name using `grep`

So according to these instructions, I did setup a user using the following commands:

1. sudo adduser john --quiet
2. addgroup nonsudo
3. addgroup john nonsudo
4. passwd john 



SSH Configuration: Generate an SSH key pair on your local machine, add the public key to your server, and configure the server to disable password-based authentication.

*Notes*

First we need to install open-ssh server:

    apt install openssh-server

    apt-get install -y openssh-server

    apk add openssh


We will generate a public/private key pair using algorythm ed25519. Option `-t` is used to select different algorythms. First we need to execute these commands on host system:

    ssh-keygen -q -t ed25519 -f $HOME/default-ssh 

It will then prompt us for password. Key pair is saved in `/home/[user]/`. Next step is to copy file to the running container. Make sure to specify path to public key, not private key.


    docker cp $HOME/default-ssh.pub [container-id]:/default-ssh.pub


Then, inside the container, we need to add the contents of public key to `authorized_keys`. If we are using Alpine inside Docker we need to create `~/.ssh` fist.

    cat /default-ssh.pub >> ~/.ssh/authorized_keys

Firewall Configuration: Set up UFW (Uncomplicated Firewall) to allow only SSH (port 22) by default. You should understand how to add additional rules when needed.

*Notes*

Before we begin, we should make sure we have a clean start with iptables:

    ### Have a clean start with iptables
  iptables -F; iptables -X
  echo 'y' | ufw reset
  echo 'y' | ufw enable
  ufw default deny incoming
  ufw default deny forward

System Updates: Update all system packages and configure automatic security updates using unattended-upgrades.

*Notes*

First we need to update indexes:

    apk update

then we can continue with upgrade:

    apk upgrade

In other linux distros where apt is used, the process for upgrading packages is similar:

    sudo apt update
    sudo apt-get update

    sudo apt upgrade
    sudo apt-get upgrade

Basic Hardening: Install and configure Fail2Ban to protect against brute-force SSH attacks.

Server Configuration: Set the correct timezone and a meaningful hostname for your server.

Service Management: Demonstrate basic systemctl commands to check the status of services, start/stop them, and enable them at boot.

Log Inspection: Use journalctl to view system logs and locate common log files in /var/log/.


