package com.bedrock.padder.model.preset;

public class PresetSchema {
    // mongoose schema wrapper, JSON V2

    /*
    * {
        "description": "desc",
        "difficulty": 1,
        "genre": "genre",
        "preset": {
          "about": {
            "bio": {
              "name": "Alan Olav Walker",
              "source": "Powered by Wikipedia X last.fm",
              "text": "Alan Walker (Alan Olav Walker) is a British-Norwegian record producer who was born in Northampton, England. He recorded electronic dance music single \"Faded\" and his song released on NoCopyrightSounds, \"Fade\".",
              "title": "Alan Walker\u0027s biography"
            },
            "color": "#00D3BE",
            "details": [
              {
                "items": [
                  {
                    "hint": "https://facebook.com/alanwalkermusic",
                    "imageId": "facebook",
                    "isHintVisible": true,
                    "isRunnableWithAnim": false,
                    "text": "facebook"
                  },
                  {
                    "hint": "https://twitter.com/IAmAlanWalker",
                    "imageId": "twitter",
                    "isHintVisible": true,
                    "isRunnableWithAnim": false,
                    "text": "twitter"
                  },
                  {
                    "hint": "https://soundcloud.com/alanwalker",
                    "imageId": "soundcloud",
                    "isHintVisible": true,
                    "isRunnableWithAnim": false,
                    "text": "soundcloud"
                  },
                  {
                    "hint": "https://instagram.com/alanwalkermusic",
                    "imageId": "instagram",
                    "isHintVisible": true,
                    "isRunnableWithAnim": false,
                    "text": "instagram"
                  },
                  {
                    "hint": "https://plus.google.com/u/0/+Alanwalkermusic",
                    "imageId": "google_plus",
                    "isHintVisible": true,
                    "isRunnableWithAnim": false,
                    "text": "google_plus"
                  },
                  {
                    "hint": "https://youtube.com/user/DjWalkzz",
                    "imageId": "youtube",
                    "isHintVisible": true,
                    "isRunnableWithAnim": false,
                    "text": "youtube"
                  },
                  {
                    "hint": "http://alanwalkermusic.no",
                    "imageId": "web",
                    "isHintVisible": true,
                    "isRunnableWithAnim": false,
                    "text": "web"
                  }
                ],
                "title": "About Alan Walker"
              },
              {
                "items": [
                  {
                    "hint": "https://soundcloud.com/alanwalker/placeholder-1",
                    "imageId": "soundcloud",
                    "isHintVisible": false,
                    "isRunnableWithAnim": false,
                    "text": "soundcloud"
                  },
                  {
                    "hint": "https://youtu.be/60ItHLz5WEA",
                    "imageId": "youtube",
                    "isHintVisible": false,
                    "isRunnableWithAnim": false,
                    "text": "youtube"
                  },
                  {
                    "hint": "https://open.spotify.com/track/1brwdYwjltrJo7WHpIvbYt",
                    "imageId": "spotify",
                    "isHintVisible": false,
                    "isRunnableWithAnim": false,
                    "text": "spotify"
                  },
                  {
                    "hint": "https://play.google.com/store/music/album/Alan_Walker_Faded?id\u003dBgdyyljvf7b624pbv5ylcrfevte",
                    "imageId": "google_play_music",
                    "isHintVisible": false,
                    "isRunnableWithAnim": false,
                    "text": "google_play_music"
                  },
                  {
                    "hint": "https://itunes.apple.com/us/album/placeholder/id1196294554?i\u003d1196294581",
                    "imageId": "apple",
                    "isHintVisible": false,
                    "isRunnableWithAnim": false,
                    "text": "apple"
                  },
                  {
                    "hint": "https://amazon.com/Faded/dp/B01NBYNKWJ",
                    "imageId": "amazon",
                    "isHintVisible": false,
                    "isRunnableWithAnim": false,
                    "text": "amazon"
                  },
                  {
                    "hint": "https://pandora.com/alan-walker/placeholder-single/placeholder",
                    "imageId": "pandora",
                    "isHintVisible": false,
                    "isRunnableWithAnim": false,
                    "text": "pandora"
                  }
                ],
                "title": "About this track"
              }
            ],
            "isTutorialAvailable": false,
            "presetArtist": "Studio Berict",
            "songArtist": "Alan Walker",
            "songName": "Faded"
          },
          "bpm": 90,
          "isGesture": true,
          "soundCount": 245,
          "tag": "alan_walker_faded_gesture"
        },
        "reviews": [
          {
            "comment": "comment",
            "date": "Sep 13, 2017 2:48:46 AM",
            "rating": 3,
            "version": 1
          }
        ],
        "version": 1
      }
    */

    private Preset preset;

    // enum [Drum and Bass, Hardcore, House, Trap, Techno, Trance, Pop, /custom]
    private String genre;

    // should add censoring
    private String description = null;

    // enum [1, 2, 3, 4, 5], defaults for 3
    private Integer difficulty = 3;

    // starts from 1
    private Integer version = 1;

    private Review reviews[] = null;

    public PresetSchema(Preset preset, String genre, String description, Integer difficulty, Integer version, Review[] reviews) {
        this.preset = preset;
        this.genre = genre;
        this.description = description;
        this.difficulty = difficulty;
        this.version = version;
        this.reviews = reviews;
    }

    public PresetSchema(Preset preset, String genre, Integer difficulty, Integer version) {
        this.preset = preset;
        this.genre = genre;
        this.difficulty = difficulty;
        this.version = version;
    }

    public PresetSchema(Preset preset, String genre) {
        this.preset = preset;
        this.genre = genre;
    }

    public Preset getPreset() {
        return preset;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public Integer getVersion() {
        return version;
    }

    public Review[] getReviews() {
        return reviews;
    }
}
