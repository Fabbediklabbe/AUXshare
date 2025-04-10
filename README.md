# Zeus Music Sharing App

Zeus is a simple and elegant music-sharing web application that allows users to share their favorite songs from platforms like YouTube, Spotify, and SoundCloud. The app is built with Java, Spring Boot, and MariaDB, and is hosted on a Raspberry Pi with SSL encryption for secure access.

## Features
- Share music via embeddable links from YouTube, Spotify, and SoundCloud
- Clean and responsive UI with song cards
- Database management with MariaDB

## Prerequisites
- Java 17
- Spring Boot 3.x
- MariaDB
- Raspberry Pi (for hosting)
- A registered domain name with correct DNS settings

## Installation
1. Clone the repository:
    ```bash
    git clone https://github.com/Fabbediklabbe/ZEUS.git
    cd ZEUS
    ```
2. Build the project:
    ```bash
    ./mvnw clean package -DskipTests
    ```

3. Configure the database:
    - Create a database in MariaDB.
    - Create and configure `application.properties` with correct credentials.

4. Run the application manually:
    ```bash
    screen -S ZEUS
    sudo java -jar target/zeus-0.0.1-SNAPSHOT.jar
    ctrl + a
    ctrl + d
    ```

## Useful Commands
- Check logs: `sudo journalctl -u zeus -f`
- Restart service: `sudo systemctl restart zeus`
- Check open ports: `sudo netstat -tulnp`

## Troubleshooting
- Ensure MariaDB is running: `sudo systemctl status mariadb`
- Ensure SSL certificates are valid: `sudo certbot certificates`
- Check if ports 80 and 443 are open in the firewall.

Enjoy sharing your favorite music with Zeus!

