import { initializeApp } from "firebase/app";
import { getFirestore } from "firebase/firestore";
const firebaseConfig = {
  apiKey: "AIzaSyBi65Lqd_tXpSPG6h9RvYboycWDhVHIKzc",
  authDomain: "roniejisa.firebaseapp.com",
  projectId: "roniejisa",
  storageBucket: "roniejisa.appspot.com",
  messagingSenderId: "983329577863",
  appId: "1:983329577863:web:28f11eeb738ac0fbe2d122",
  databaseURL:
    "https://roniejisa-default-rtdb.asia-southeast1.firebasedatabase.app",
  measurementId: "G-1D0PDK19V6"
};

initializeApp(firebaseConfig);
const db = getFirestore();
export default db;
