document.addEventListener("DOMContentLoaded", function () {
    console.log("JavaScript laddat!");
    fetchSongs(); // Ladda låtar vid sidans start

    const isAuthenticated = document.body.dataset.authenticated === "true";

    const form = document.getElementById("add-song-form");
    const loginReminder = document.getElementById("login-reminder");

    if (!isAuthenticated) {
        if (form) form.style.display = "none";
        if (loginReminder) loginReminder.style.display = "block";
    } else {
        if (loginReminder) loginReminder.style.display = "none";
    }

    if (form && isAuthenticated) {
        form.addEventListener("submit", function (event) {
            event.preventDefault();

            const username = document.getElementById("username").value;
            const title = document.getElementById("title").value.trim();
            const artist = document.getElementById("artist").value.trim();
            const link = document.getElementById("link").value.trim();

            if (!title || !artist || !link) {
                alert("Vänligen fyll i alla fält.");
                return;
            }

            const newSong = { title, artist, link };

            fetch(`/api/songs?username=${encodeURIComponent(username)}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(newSong)
            })
            .then(response => {
                if (response.ok) {
                    document.getElementById("message").innerText = "Låten har lagts till!";
                    form.reset();
                    fetchSongs();
                } else {
                    document.getElementById("message").innerText = "Fel vid tilläggning av låt.";
                }
            })
            .catch(error => console.error("Fel vid tilläggning:", error));
        });
    }

    function fetchSongs() {
        fetch("/api/songs")
            .then(response => response.json())
            .then(songs => updateSongList(songs))
            .catch(error => console.error("Fel vid hämtning av låtar:", error));
    }

    function updateSongList(songs) {
        const songList = document.getElementById("song-list");
        songList.innerHTML = "";

        if (songs.length === 0) {
            songList.innerHTML = `<p>Inga låtar hittades.</p>`;
            return;
        }

        songs.forEach(song => {
            const songElement = document.createElement("div");
            songElement.classList.add("song");
            songElement.innerHTML = generateSongHTML(song);

            const deleteButton = document.createElement("button");
            deleteButton.classList.add("delete-button");
            deleteButton.innerText = "🗑️";
            deleteButton.setAttribute("data-id", song.id);
            deleteButton.onclick = () => confirmDelete(song.id);

            songElement.appendChild(deleteButton);
            songList.appendChild(songElement);
        });
    }

    function generateSongHTML(song) {
        let songHTML = `<strong>${song.title || "Okänd titel"}</strong>`;
        songHTML += `<br><small>${song.artist || "Okänd artist"}</small>`;

        if (song.link) {
            if (song.link.includes("spotify.com/track")) {
                const spotifyEmbedUrl = song.link.replace("open.spotify.com/track", "open.spotify.com/embed/track");
                songHTML += `<br><iframe src="${spotifyEmbedUrl}" width="300" height="80" frameborder="0" allowtransparency="true" allow="encrypted-media"></iframe>`;
            } else if (song.link.includes("soundcloud.com")) {
                songHTML += `<iframe src="https://w.soundcloud.com/player/?url=${encodeURIComponent(song.link)}"></iframe>`;
            } else if (song.link.includes("youtube.com") || song.link.includes("youtu.be")) {
                songHTML += `<div class="video-container">${generateYouTubeEmbed(song.link)}</div>`;
            } else {
                songHTML += `<br><a href="${song.link}" target="_blank">🎵 Lyssna</a>`;
            }
        }

        if (song.addedAt) {
            songHTML += `<br><small>Tillagd: ${new Date(song.addedAt).toLocaleString()}</small>`;
        }

        songHTML += `<br><small>Uppladdad av: <strong>${song.username || "Okänd"}</strong></small>`;
        return songHTML;
    }

    function confirmDelete(songId) {
        if (confirm("Är du säker på att du vill ta bort denna låt?")) {
            deleteSong(songId);
        }
    }

    function deleteSong(songId) {
        fetch(`/api/songs/${songId}`, { method: "DELETE" })
            .then(response => {
                if (response.ok) {
                    alert("Låten har tagits bort.");
                    fetchSongs();
                } else {
                    alert("Fel vid borttagning av låt.");
                }
            })
            .catch(error => console.error("Fel vid borttagning:", error));
    }

    function extractYouTubeId(url) {
        const match = url.match(/(?:youtube\.com\/(?:[^\/]+\/.+\/|(?:v|e(?:mbed)?)\/|.*[?&]v=)|youtu\.be\/)([^"&?\/\s]{11})/);
        return match ? match[1] : "";
    }

    function generateYouTubeEmbed(url) {
        const videoId = extractYouTubeId(url);
        return videoId
            ? `<iframe width="300" height="200" src="https://www.youtube.com/embed/${videoId}" frameborder="0" allowfullscreen></iframe>`
            : "";
    }
});
