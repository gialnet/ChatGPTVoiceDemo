package vivaldispring.eu.chatgptvoicedemo.components;

/*
curl https://api.openai.com/v1/audio/transcriptions \
  -H "Authorization: Bearer $OPENAI_API_KEY" \
  -H "Content-Type: multipart/form-data" \
  -F file="@/path/to/file/audio.mp3" \
  -F model="whisper-1"
 */
public interface AudioToText {

    String transcribeAudio(String audioFile);

}
