import React, { useState } from 'react';

function DeleteUser ({users, onDelete}) {
    const [selectedUser, setSelectedUser] = useState(null);
    
    const handleDeleteUser = async () =>{
        if (!selectedUser){
            alert('Please select a user to delete.');
            return;
        }

        try{
            const response = await fetch(`http://localhost:8081/api/users/${selectedUser}`, {
                method: 'DELETE',
            });
            if (!response.ok) {
                const errorMessage = await response.text(); 
                throw new Error(errorMessage);
            }
            alert(`User ${selectedUser} deleted.`);
            setSelectedUser(null); 
            onDelete();
        }
        catch (error){
            console.error('Error deleting users: ', error);
        }
    };

    return(
    <div>
        <h2>Delete User</h2>
            <table>
                <thead>
                    <tr>
                        <th>Username</th>
                        <th>Role</th>
                        <th>Select</th> 
                    </tr>
                </thead>
                <tbody>
                    {users.map((user, index) => (
                        <tr key={index}>
                            <td>{user.username}</td>
                            <td>{user.role}</td>
                            <td>
                                <input
                                    type="radio"
                                    name="userSelect"
                                    value={user.username}
                                    onChange={() => setSelectedUser(user.username)}
                                />
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <button onClick={handleDeleteUser}>Delete Selected User</button>
        </div>
    );
}
    
export default DeleteUser;
    