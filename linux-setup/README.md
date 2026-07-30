# Linux server setup

Inspired by [roadmap.sh project](https://roadmap.sh/projects/linux-server-setup)

## Prerequsites

Install Docker

Run docker container Linux Alpine:

    docker run -d nginx:alpine

After creating it, ssh into container.


## Requirements

You are required to perform the following tasks on a fresh Ubuntu server:

- User Setup: Create a non-root user with sudo privileges. This user should be used for all future server administration instead of root.

Creating an user is done using the following command:

    adduser [USERNAME]


You can add or remove users sudo permisisons by adding or removing it from the "sudo" group:

    usermod -aG [GROUP] -p [PASSWORD] [USERNAME]

    or

    addgroup [username] [group]

It is not recommended to add sudo permissions to another user. If you want, you can create a new group for users:

    groupadd -g 10000 [GROUP]

You can verify that these were added by running:


    getent passwd // displays active users and their home dirs
    getent group // displays groups and their members

The same information is also available in `/etc/passwd` and `/etc/group`. When printing this content, you can filter out results by username or group name using `grep`

So according to these instructions, I did setup a user using the following commands:

1. sudo adduser john --quiet
2. addgroup nonsudo
3. addgroup john nonsudo



SSH Configuration: Generate an SSH key pair on your local machine, add the public key to your server, and configure the server to disable password-based authentication.

Firewall Configuration: Set up UFW (Uncomplicated Firewall) to allow only SSH (port 22) by default. You should understand how to add additional rules when needed.

System Updates: Update all system packages and configure automatic security updates using unattended-upgrades.

Basic Hardening: Install and configure Fail2Ban to protect against brute-force SSH attacks.

Server Configuration: Set the correct timezone and a meaningful hostname for your server.

Service Management: Demonstrate basic systemctl commands to check the status of services, start/stop them, and enable them at boot.

Log Inspection: Use journalctl to view system logs and locate common log files in /var/log/.


