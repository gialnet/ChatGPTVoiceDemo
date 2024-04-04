package vivaldispring.eu.chatgptvoicedemo.components;

import vivaldispring.eu.chatgptvoicedemo.data.TranscriptText;

import java.io.InputStream;

/*
curl https://api.openai.com/v1/audio/transcriptions \
  -H "Authorization: Bearer $OPENAI_API_KEY" \
  -H "Content-Type: multipart/form-data" \
  -F file="@/path/to/file/audio.mp3" \
  -F model="whisper-1"
 */
public interface AudioToText {

    TranscriptText ApacheTranscriberInMemory(InputStream AudioFile);

}
