package com.practicum.playlistmaker.library.data

import com.practicum.playlistmaker.player.data.db.entity.AddedTrackEntity
import com.practicum.playlistmaker.search.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.domain.models.Track

class TrackDbConvertor {

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isFavorite = track.isFavorite,
        )
    }

    fun map(track: TrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite,
        )
    }

    fun mapAdded(addedTrack: AddedTrackEntity): Track {
        return Track(
            addedTrack.trackId,
            addedTrack.trackName,
            addedTrack.artistName,
            addedTrack.trackTimeMillis,
            addedTrack.artworkUrl100,
            addedTrack.collectionName,
            addedTrack.releaseDate,
            addedTrack.primaryGenreName,
            addedTrack.country,
            addedTrack.previewUrl,
            addedTrack.isFavorite,
        )
    }
}