# ChatGPT voice demo

This project demonstrates how to leverage the ChatGPT API to transcribe audio captured from the microphone on an HTML page. The transcribed text is then sent back to the HTML page via Server-Sent Events (SSE). This implementation represents a voice interface that can be integrated into various projects, enabling users to interact with applications using voice commands.

The process flow is as follows:

An HTML page (microphone.html) captures audio from the user's microphone using the Web Audio API.
The captured audio data is sent to a server-side endpoint for processing.
On the server-side, the audio data is forwarded to the ChatGPT API for transcription.
The ChatGPT API transcribes the audio and returns the text transcription.
The server-side code receives the transcribed text and sends it back to the HTML page using Server-Sent Events (SSE).
The HTML page receives the transcribed text and can display it or perform further actions based on the voice input.
After receiving the transcribed text, you can invoke specific functions or trigger actions within your application to manage its behavior based on the voice commands provided by the user.

This project showcases the integration of voice recognition capabilities using the ChatGPT API, enabling the development of voice-controlled user interfaces for various applications. It demonstrates how to capture audio from the microphone, leverage the ChatGPT API for transcription, and transmit the transcribed text back to the client-side using Server-Sent Events (SSE). 
