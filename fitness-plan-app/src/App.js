// App.js
import React, { useState } from 'react';
import Login from './Login';
import SignUp from './SignUp';

function App() {
    const [view, setView] = useState('welcome');
    const [currentUser, setCurrentUser] = useState(null);
    const [isAdmin, setIsAdmin] = useState(false);
    const [fitnessPlans, setFitnessPlans] = useState([]);

    const handleLogin = (username, role) => {
        setCurrentUser(username);
        setIsAdmin(role === "Admin");
        setView(role === "Admin" ? 'adminDashboard' : 'userDashboard');
        if (role !== "Admin") fetchFitnessPlans();
    };

    const fetchFitnessPlans = async () => {
        try {
            const response = await fetch('http://localhost:8081/api/fitness/recommend?fitnessGoal=Weight Loss&fitnessLevel=Beginner');
            const plans = await response.json();
            setFitnessPlans(plans);
        } catch (error) {
            console.error('Error fetching fitness plans:', error);
        }
    };

    return (
        <div className="App">
            <header className="App-header">
                <h1>Fitness Plan Recommendation System</h1>
                {view === 'welcome' && (
                    <div>
                        <button onClick={() => setView('login')}>Login</button>
                        <button onClick={() => setView('signup')}>Sign Up</button>
                    </div>
                )}
                {view === 'login' && <Login onLogin={handleLogin} />}
                {view === 'signup' && <SignUp onSignUpComplete={() => setView('login')} />}
                {view === 'userDashboard' && (
                    <div>
                        <h2>Welcome, {currentUser}!</h2>
                        <h3>Your Recommended Fitness Plans:</h3>
                        <ul>
                            {fitnessPlans.map((plan, index) => (
                                <li key={index}>
                                    {plan.planType} - {plan.duration} mins/week - {plan.healthGoal}
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
                {view === 'adminDashboard' && (
                    <div>
                        <h2>Admin Dashboard</h2>
                        <p>Admin privileges and settings go here.</p>
                    </div>
                )}
            </header>
        </div>
    );
}

export default App;
