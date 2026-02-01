package com.group.game.dashdash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class AudioManager {

    private List<String> playlist = new ArrayList<>();
    private String lastPlayedSong = "";
    private double musicTimer = 0;
    private double currentSongDuration = 0;
    private boolean playlistStarted = false;

    private double fadeMultiplier = 0;
    private double userMenuVolume = 1.0;

    public AudioManager() {
        // Initialize your playlist
        playlist.add("TTEN.wav");
        playlist.add("LELN.wav");
        playlist.add("JANA.wav");
    }

    public void startPlaylist() {
        playNextSong();
    }

    private void playNextSong() {
        if (playlist.isEmpty()) return;

        getAudioPlayer().stopAllMusic();
        fadeMultiplier = 0;
        userMenuVolume = getSettings().getGlobalMusicVolume();
        getSettings().setGlobalMusicVolume(0);

        List<String> availableSongs = new ArrayList<>(playlist);
        if (availableSongs.size() > 1) {
            availableSongs.remove(lastPlayedSong);
        }

        Collections.shuffle(availableSongs);
        String nextSong = availableSongs.get(0);
        lastPlayedSong = nextSong;

        try {
            var music = getAssetLoader().loadMusic(nextSong);
            getAudioPlayer().playMusic(music);
        } catch (Exception e) {
            System.out.println("Playlist Error: " + nextSong);
        }

        musicTimer = 0;
        playlistStarted = true;

        // Using a switch is cleaner as you add more music
        currentSongDuration = switch (nextSong) {
            case "TTEN.wav" -> 95;
            case "LELN.wav" -> 80;
            case "JANA.wav" -> 93;
            default -> 100;
        };
    }

    // Call this in GGApplication's onUpdate
    public void onUpdate(double tpf) {
        if (!playlistStarted) return;

        musicTimer += tpf;

        if (musicTimer >= (currentSongDuration - 3.0)) {
            fadeMultiplier -= tpf * 0.35;
        } else if (musicTimer <= 3.0) {
            fadeMultiplier += tpf * 0.35;
        } else {
            fadeMultiplier = 1.0;
        }

        fadeMultiplier = Math.max(0, Math.min(1, fadeMultiplier));

        if (fadeMultiplier < 1.0) {
            getSettings().setGlobalMusicVolume(userMenuVolume * fadeMultiplier);
        } else {
            userMenuVolume = getSettings().getGlobalMusicVolume();
        }

        if (musicTimer >= currentSongDuration) {
            playlistStarted = false;
            playNextSong();
        }
    }

    // --- SOUND EFFECTS METHODS ---
    public void playJumpSound() {
        play("jump_sfx.wav"); // FXGL DSL method for short sounds
    }

    public void playCrashSound() {
        play("crash.wav");
    }
}