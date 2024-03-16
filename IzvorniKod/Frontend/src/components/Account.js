import React from "react";


export default function Account(props) {

    var user = JSON.parse(localStorage.getItem('user'));
    const {firstAndLastName, phoneNumber, email, country, companyName, address, username, detailsOfParticipation, userAccountRole } = props.account;
    
    return (
        <tr>
            <td>{firstAndLastName}</td>
            <td>{username}</td>
            <td>{email}</td>
            <td>{country}</td>
            <td>{address}</td>
            <td>{companyName}</td>
            <td>{phoneNumber}</td>
            <td>{detailsOfParticipation}</td>
            <td>{userAccountRole}</td>
      </tr>
    );
}