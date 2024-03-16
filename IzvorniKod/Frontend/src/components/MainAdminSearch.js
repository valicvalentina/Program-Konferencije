import React from "react";
import {useState} from 'react';
import { Checkbox } from "semantic-ui-react";
import Account from "./Account";
import '../styles/MainAdminSearch.css';
import FilterByCountry from "./FilterByCountry";
import FilterByAccountType from "./FilterByAccountType";
import axios from "axios";
import { Link } from "react-router-dom";
import {Button, Grid} from 'semantic-ui-react'

export default function MainAdminSearch() {


    const [participantAccounts, setparticipantAccounts] = useState([]);
    let [filteredParticipantAccounts, setFilteredParticipantAccounts] = useState([]);

    const [operativeAccounts, setOperativeAccounts] = useState([]);
    let [filteredOperativeAccounts, setFilteredOperativeAccounts] = useState([]);

    const [selectedType, setSelectedType] = useState('all');
    const [groupedByCountry, setGroupedByCountry] = useState([]);


    var user = JSON.parse(localStorage.getItem('user'));
    const token = JSON.parse(localStorage.getItem('token'));

    const config= {
        headers: { Authorization : `Bearer ${token.token}`}
    }

    React.useEffect(() => {

        axios.get(`/api/myConference/${user.conferenceId}/participants`,
                    config)
        .then(response =>  {setparticipantAccounts(response.data)
                            return response.data})
        .then(participantAccounts => setFilteredParticipantAccounts(participantAccounts))
    }, []);

    React.useEffect(() => {

        axios.get(`/api/myConference/${user.conferenceId}/operative`,
                    config)
        .then(response =>  {setOperativeAccounts(response.data)
                            return response.data})
        .then(operativeAccounts => setFilteredOperativeAccounts(operativeAccounts))
    }, []);

    React.useEffect(() => {
        axios.get(`/api/users/getByCountry/${user.conferenceId}`,
                    config)
        .then(response => { return response.data})
        .then(map => {
            setGroupedByCountry(map)
        })
    }, []);

    function onFilterValueTypeSelected(filterValue){
        setSelectedType(filterValue);
    }

    
    function onFilterCountrySelected(countryFilter) {
        
        if(countryFilter === 'allCountries') {
            setFilteredOperativeAccounts(operativeAccounts);
            setFilteredParticipantAccounts(participantAccounts);
            return;
        }

        
        setFilteredOperativeAccounts(operativeAccounts);
        setFilteredParticipantAccounts(participantAccounts);

        setFilteredOperativeAccounts((current) =>
        current.filter((acc) => acc.country === countryFilter));
        setFilteredParticipantAccounts((current) =>
        current.filter((acc) => acc.country === countryFilter));
        
    }


    return(
    <>
     <section style={{margin:50}}>
        <Grid.Row>
            <Link to="/myConference">
                <Button color = 'teal' style={{width:200}}>MY CONFERENCE</Button>
            </Link>
        </Grid.Row>
        <h1>TABLE OF PARTICIPANTS</h1>
        <hr></hr>
        <div className="button-container">
            <FilterByAccountType filterValueSelected={onFilterValueTypeSelected}></FilterByAccountType>
            <FilterByCountry  filterCountrySelected={onFilterCountrySelected}></FilterByCountry>
        </div>
        <div className="tbl-header">
            <table cellPadding="0" cellSpacing="0" border="0">
            <thead>
                <tr>
                <th>Name</th>
                <th>Username</th>
                <th>Email</th>
                <th>Country</th>
                <th>Address</th>
                <th>Company</th>
                <th>Phone number</th>
                <th>Details</th>
                <th>Account Role</th>
                </tr>
            </thead>
            </table>
        </div>
        <div className="tbl-content">
            <table cellPadding="0" cellSpacing="0" border="0">
            <tbody>
                {(selectedType === 'participant' || selectedType === 'all') &&
                (filteredParticipantAccounts.map((account) => <Account key={account.username} account={account}/>))}
                {(selectedType ==='operative' || selectedType === 'all')  &&
                (filteredOperativeAccounts.map((account) => <Account key={account.username} account={account}/>))}
            </tbody>
            </table>
        </div>
        </section>   
            </>
            )

};