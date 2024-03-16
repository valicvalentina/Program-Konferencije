import React from "react";
import {useState} from 'react';
import axios from "axios";

function FilterByCountry(props) {

    const [countries, setCountries] = React.useState([]);
    const [groupedByCountry, setGroupedByCountry] = React.useState([]);

    var user = JSON.parse(localStorage.getItem('user'));
    const token = JSON.parse(localStorage.getItem('token'));

    const config= {
        headers: { Authorization : `Bearer ${token.token}`}
    }


    React.useEffect(() => {
        axios.get(`/api/users/getByCountry/${user.conferenceId}`,
                    config)
        .then(response =>  {return response.data})
        .then(map => {
            setCountries(Object.keys(map))
        })
    }, []);

    function onFilterCountryChanged(event) {
        props.filterCountrySelected(event.target.value)
    }

    return(<div>
        <select className="ui selection dropdown" onChange={onFilterCountryChanged}>
            <option value="allCountries" key="allCountries" name="allCountries">Select a country</option>
            {countries.map((country) => <option value={country}   key={country}    name={country} >{country} </option>)}
            </select>
    </div>);
}

export default FilterByCountry;