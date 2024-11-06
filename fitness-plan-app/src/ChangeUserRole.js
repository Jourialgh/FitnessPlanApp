import React from 'react';

function ChangeUserRole ({users, onRoleChange}){
    const changeUserRole = async (username, newRole) => {
        try{
        await fetch(`http://localhost:8081/api/users/update-role?username=${username}&role=${newRole}`, {
            method: 'PUT',
            headers:{
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({role: newRole}),
        });
        alert(`User ${username}'s role updated to ${newRole}.`);
        onRoleChange();
    }
    catch(error) {
        console.error('Error updating user role:', error);
    }
};

return (
    <div>
        <h2>Change User Role</h2>
        <ul>
            {users.map((user, index) => (
                <li key={index}>
                    {user.username} - {user.role}
                    <button onClick={() => changeUserRole(user.username, user.role === 'Admin' ? 'Regular' : 'Admin')}>
                        Change Role to {user.role === 'Admin' ? 'Regular' : 'Admin'}
                    </button>
                </li>
            ))}
        </ul>
    </div>
);
}

export default ChangeUserRole;