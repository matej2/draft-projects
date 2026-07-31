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

Installation of UFW:

    apt install ufw

Before we begin, we should make sure we have a clean start with iptables:

    ### Have a clean start with iptables
    iptables -F; iptables -X
    echo 'y' | ufw reset
    echo 'y' | ufw enable
    ufw default deny incoming
    ufw default deny forward

We can then allow traffic on specific ports:

    ufw allow ssh

System Updates: Update all system packages and configure automatic security updates using unattended-upgrades.

*Notes*

First we need to update package indexes:

    apk update

then we can continue with upgrade:

    apk upgrade

In other linux distros where apt is used, the process for upgrading packages is similar:

    sudo apt update
    sudo apt-get update

    sudo apt upgrade
    sudo apt-get upgrade

We can setup unattended upgrades by installing the following package:

    apt install unattended-upgrades

Configuration files are found in directory:

    cd /etc/apt/apt.conf.d

Here we can further configure unattended upgrades by editing the config file:

    vi 50unattended-upgrades

The most common config options you want to set are:

- Allowed origins: Here we have the option to enable updates of different types. By default, only security updates are enabled. We can uncomment certain origins to allow other types of upgrades.

- AutoFixInterruptedDpkg: If a package installation comes to an unclean dpkg exit, this command will try to automatically fix configuration.

- Remove-unused-dependencies: This option removes unused dependencies after an upgrade.

- Auto-reboot: Restarts the machine if an update requires it. Auto-reboot-withUsers enables restarts even if users have active sessions. Automatic-reboot-time sopecifies a specific time for restart

- Mail: Sends email notifications regarding upgrades. We can set email address where these emails are sent to. Besides that, we also need to configure SMTP server on our machine.

Finally restart the service:

    sudo systemctl restart unattended-upgrades

    sudo systemctl status unattended-upgrades

    unattended-upgrade --debug // or start manually

Basic Hardening: Install and configure Fail2Ban to protect against brute-force SSH attacks.

*Notes*

Fail2ban is used to protect SSH against attachs. If a user makes too many wrong passwords attempts, this service will create a firewall rule to block this IP. After some time, it will release IP from firewall.

Configuration is located in this directory:

    cd /etc/fail2ban

First we need to make copies of configuration files. This ensures the contents will not be overritten:

    cp fail2ban.conf fail2ban.local
    cp jail.conf jail.local

Relavant config:

- IgnoreIP: We can set ip list that the service will ignore. For example, a machine that the admin will use to connect to server.

- Bantime: Amount of time to ban host

- Findtime: Host is banned if a certain number of retries in reached in findtime

- Maxretry: Number of failures

- [...] sections: SSHD is very important module to protect. Is is advised to only enable config that is appliable to our case - only enable for services that are running on our server.

Server Configuration: Set the correct timezone and a meaningful hostname for your server.

Service Management: Demonstrate basic systemctl commands to check the status of services, start/stop them, and enable them at boot.

Log Inspection: Use journalctl to view system logs and locate common log files in /var/log/.


