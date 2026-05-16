importScripts('https://www.gstatic.com/firebasejs/10.13.2/firebase-app-compat.js');
importScripts('https://www.gstatic.com/firebasejs/10.13.2/firebase-messaging-compat.js');

firebase.initializeApp({
    apiKey: "AIzaSyANL5S0DVVuXSKLaafGhZc8t3O6ZYLWZpE",
    authDomain: "airmonitoringandgasdetection.firebaseapp.com",
    projectId: "airmonitoringandgasdetection",
    storageBucket: "airmonitoringandgasdetection.firebasestorage.app",
    messagingSenderId: "274729974150",
    appId: "1:274729974150:web:6709cdd4ce52b1fcbf5407",
    measurementId: "G-KSH0L60JSN"
});

const messaging = firebase.messaging();

messaging.onBackgroundMessage(function(payload) {
    console.log('[firebase-messaging-sw.js] Received background message ', payload);

    const notificationTitle = payload.notification.title;
    const notificationOptions = {
        body: payload.notification.body,
        icon: '/icon.png'
    };

    self.registration.showNotification(notificationTitle, notificationOptions);
});
