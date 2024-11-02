import React, { useState } from 'react';

function SignUp({ onSignUpComplete }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('Regular');
    const [medicalHistory, setMedicalHistory] = useState('');

    const handleSignUp = async () => {
        const user = { username, password, role, medicalHistory };
        try {
            const response = await fetch('http://localhost:8081/api/users/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(user),
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const message = await response.text();
            alert(message);
            if (message.includes("User registered successfully")) {
                onSignUpComplete();
            }
        } catch (error) {
            console.error('Error signing up:', error);
            alert('An error occurred. Please try again.');
        }
    };

    return (
        <div>
            <h2>Sign Up</h2>
            <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <select value={role} onChange={(e) => setRole(e.target.value)}>
                <option value="Regular">Regular</option>
                <option value="Admin">Admin</option>
            </select>
            <textarea
                placeholder="Medical History"
                value={medicalHistory}
                onChange={(e) => setMedicalHistory(e.target.value)}
            />
            <button onClick={handleSignUp}>Sign Up</button>
        </div>
    );
}

export default SignUp;
