import React from "react";
import MainAdminSearch from "./MainAdminSearch";
import FilterByCountry from "./FilterByCountry";
import Account from "./Account";
import { useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom/cjs/react-router-dom.min";
import { Link } from "react-router-dom";
import {Button, Grid} from "semantic-ui-react";

function ShowAttendees() {

    const {id} = useParams();

    const [attendeesAccounts, setAttendeesAccounts] = useState([]);
    let [filteredAttendeesAccounts, setFilteredAttendeesAccounts] = useState([]);

    const [groupedByCountry, setGroupedByCountry] = useState([]);


    var user = JSON.parse(localStorage.getItem('user'));
    const token = JSON.parse(localStorage.getItem('token'));

    const config= {
        headers: { Authorization : `Bearer ${token.token}`}
    }

    React.useEffect(() => {
        axios.get(`/api/specialEvents/attending/${id}`,
                    config)
        .then(response =>  {setAttendeesAccounts(response.data)
                            return response.data})
        .then(attendeesAccounts => setFilteredAttendeesAccounts(attendeesAccounts))
    }, []);

    function onFilterCountrySelected(countryFilter) {
        
        if(countryFilter === 'allCountries') {
            setFilteredAttendeesAccounts(attendeesAccounts);
            return;
        }

        
        setFilteredAttendeesAccounts(attendeesAccounts);

        setFilteredAttendeesAccounts((current) =>
        current.filter((acc) => acc.country === countryFilter));
        
    }


    return(
        <>
         <section style={{margin: 50}}>
            <Grid.Row>
                <Link to="/myConference">
                    <Button color = 'teal' style={{width:200, margin: 20}}>MY CONFERENCE</Button>
                </Link>
             </Grid.Row>
             <h1>Attending participants</h1>
            <FilterByCountry  filterCountrySelected={onFilterCountrySelected}></FilterByCountry>
            
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
                    {(filteredAttendeesAccounts.map((account) => <Account key={account.username} account={account}/>))}
                </tbody>
                </table>
            </div>
            </section>   
                </>
                )
}

export default ShowAttendees;