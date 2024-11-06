// App.js
import React, { useState } from 'react';
import Login from './Login';
import SignUp from './SignUp'; 
import DeleteUser from './DeleteUser';
import ChangeUserRole from './ChangeUserRole';
import ViewAllUsers from './ViewAllUsers';

function App() {
    const [view, setView] = useState('welcome');
    const [currentUser, setCurrentUser] = useState(null);
    const [isAdmin, setIsAdmin] = useState(false);
    const [fitnessPlans, setFitnessPlans] = useState([]);
    const [selectedAdminAction, setSelectedAdminAction] = useState('viewAllUsers');
    const [users,setUsers] = useState([]);

    const handleLogin = (username, role) => {
        setCurrentUser(username);
        setIsAdmin(role === "Admin");
        setView(role === "Admin" ? 'adminDashboard' : 'userDashboard');
        if (role !== "Admin") fetchFitnessPlans();
        if (role === "Admin") fetchAllUsers();
    };

    const handleLogout = () => {
        setCurrentUser(null);
        setIsAdmin(false);
        setFitnessPlans([]);
        setView('welcome');
    }

    const fetchFitnessPlans = async () => {
        try {
            const response = await fetch('http://localhost:8081/api/fitness/recommend?fitnessGoal=Weight Loss&fitnessLevel=Beginner');
            const plans = await response.json();
            setFitnessPlans(plans);
        } catch (error) {
            console.error('Error fetching fitness plans:', error);
        }
    };

    const fetchAllUsers = async() => {
        const username = 'admin'; 
        const password = 'password';
        const token = btoa(`${username}:${password}`); 

        try{
            const response =await fetch(`http://localhost:8081/api/users/all`, {
                method: 'GET',
                headers: {
                    'Authorization': `Basic ${token}`,
                    'Content-Type': 'application/json',
                },
                credentials: 'include'
            });
            if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
            const usersData = await response.json();
            console.log('Fetched users:', usersData);
            setUsers(usersData);   
        }
        catch (error){
            console.error('Error fetching users: ', error);
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
                        <button onClick={handleLogout}>Logout</button>
                    </div>
                )}
                {view === 'adminDashboard' && (
                    <div className="admin-dashboard">
                        <div className="sidenav">
                            <button onClick={() => setSelectedAdminAction('viewAllUsers')}>View All Users</button>
                            <button onClick={() => setSelectedAdminAction('handleDeleteUser')}>Delete User</button>
                            <button onClick={() => setSelectedAdminAction('changeUserRole')}>Change User Role</button>
                        </div>
                        <div className="admin-content">
                            {selectedAdminAction === 'viewAllUsers' && <ViewAllUsers users={users} />}
                            {selectedAdminAction === 'handleDeleteUser' && <DeleteUser users={users} onDelete={fetchAllUsers} />}
                            {selectedAdminAction === 'changeUserRole' && <ChangeUserRole users={users} onRoleChange={fetchAllUsers} />}
                        </div>
                        <button onClick={handleLogout}>Logout</button>
                    </div>
                )}
            </header>
        </div>
    );
}

export default App;
