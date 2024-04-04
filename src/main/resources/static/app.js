document.addEventListener('DOMContentLoaded', function() {
    let isRecording = false;
    let mediaRecorder;
    let audioChunks = [];

    const toggleMicButton = document.getElementById('toggleMic');
    // Create an audio element for playback
    let audioElement = document.createElement('audio');
    audioElement.controls = true;

    // Optionally, append the audio element to the body or a specific element in your page
    // document.body.appendChild(audioElement);

    toggleMicButton.addEventListener('click', function() {
        if (!isRecording) {
            navigator.mediaDevices.getUserMedia({ audio: true })
                .then(stream => {

                    audioChunks = [];

                    mediaRecorder.addEventListener('dataavailable', event => {
                        audioChunks.push(event.data);
                    });

                    toggleMicButton.textContent = 'Stop Microphone';
                    toggleMicButton.classList.remove('bg-blue-500', 'hover:bg-blue-700');
                    toggleMicButton.classList.add('bg-red-500', 'hover:bg-red-700');

                    mediaRecorder.addEventListener('stop', () => {
                        const audioBlob = new Blob(audioChunks, { 'type' : 'audio/mp3' });
                        const formData = new FormData(); // Correct scope for formData
                        formData.append('file', audioBlob, 'audio.webm');
                        const audioUrl = URL.createObjectURL(audioBlob);

                        // Set the src of the audioElement to the audioUrl and play it
                        audioElement.src = audioUrl;
                        audioElement.play();

                        fetch('http://localhost:8080/api/upload2', {
                            method: 'POST',
                            body: formData,
                        })
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Network response was not ok');
                                }
                                return response.json();
                            })
                            .then(data => console.log(data))
                            //.catch(error => console.error("Error uploading the file:", error));
                    });

                    isRecording = true;
                })
                .catch(error => {
                    console.error("Error accessing the microphone", error);
                    alert("Failed to access the microphone. Please ensure it is not in use by another application and try again.");
                });
        } else {
            mediaRecorder.stop();
            toggleMicButton.textContent = 'Start Microphone';
            toggleMicButton.classList.remove('bg-red-500', 'hover:bg-red-700');
            toggleMicButton.classList.add('bg-blue-500', 'hover:bg-blue-700');
            isRecording = false;
        }
    });
});

