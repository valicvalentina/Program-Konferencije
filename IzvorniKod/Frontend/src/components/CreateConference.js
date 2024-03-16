import React from 'react';
import { Button, Form, Grid, Header, MenuItem, Segment } from 'semantic-ui-react';
import { Link } from 'react-router-dom';
import { MdBusinessCenter } from "react-icons/md";
import { useHistory } from 'react-router-dom/cjs/react-router-dom.min';
import countries from "i18n-iso-countries";
import axios from 'axios';

export default function CreateConference() {
    const [form, setForm] = React.useState({name:'', city:'', description:'',dateTimeStart: '', dateTimeEnd: '', topics: ''});

    const [country, setCountry] = React.useState('');

    countries.registerLocale(require("i18n-iso-countries/langs/en.json"));

    const countryObj = countries.getNames("en", {select : "official"});

    const countryArr = Object.entries(countryObj).map(([key, value]) => {
        return {
          key : value,
          value : value,
          text: value
        };
    });

   
    function onChange(event) {
      const {name, value} = event.target;
      console.log(event.target.value)
      setForm(oldForm => ({...oldForm, [name]: value}))
     }

     function onCountrySelect(e, {value}) {
      setCountry(value)
      console.log('Drzava ', country)
      console.log('Vrijednost ', value)
     }

    const history = useHistory(); 

    const routeChangeSuccesful = function() {
      history.push('/register');
    }

    const token = JSON.parse(localStorage.getItem("token"));;

     async function onSubmit(e) {
      e.preventDefault();
     const data = {
          name: form.name,
          city: form.city,
          country: country,
          description: form.description,
          dateStart: form.dateTimeStart,
          dateEnd: form.dateTimeEnd,
          topics: form.topics
      }
      const config = {
        headers: { Authorization: `Bearer ${token.token}`, 'Content-Type': 'application/json'}
      };
    
    axios.post( 
      '/api/',
      JSON.stringify(data),
      config
    ).then( function(response) {
        	    console.log(response);
              alert('Conference was succesfully created. Please continue to set the main admin for the conference');
              routeChangeSuccesful();
    }).catch( function(error) {
              alert('Something went wrong while creating the conference. Try again.');
              console.log(error);
    });
    }

    return (
        <div>
        <div style={{width: "250px", margin: "10px"}}>
          <Link to='/'>
            <Button color='teal' fluid size='large'>
              Go Back To Home Page
            </Button>
          </Link>              
        </div>
        <Grid textAlign='center' style={{ height: '100vh' }} verticalAlign='middle'>
        <Grid.Column style={{ maxWidth: 450 }}>
          <Header as='h2' color='teal' textAlign='center'>
            <div>
            <MdBusinessCenter style={{ fontSize: '80px'}}/>
            </div>
            Create new conference!
          </Header>
          <Form size='large' onSubmit={onSubmit}>
            <Segment stacked>
              <Form.Input fluid name = 'name'  placeholder='Name' onChange={onChange} defaultValue={form.name}/>
              <Form.Select fluid name = 'country'  placeholder='Country' onChange={onCountrySelect} multiple={false} value={country} options={countryArr}/>
              <Form.Input fluid name = 'city'  placeholder='Place' onChange={onChange} defaultValue={form.city}/>
              <Form.Input fluid name = 'description'  placeholder='Description' onChange={onChange} defaultValue={form.description}/>
              <Form.Input type="text" placeholder="Start date" onFocus={(e) => (e.currentTarget.type = "date")}
                 fluid name = 'dateTimeStart' onChange={onChange} defaultValue={form.dateTimeStart}/>
              <Form.Input type='text' onFocus={(e) => (e.currentTarget.type = "date")} fluid name = 'dateTimeEnd'  placeholder='End date' onChange={onChange} defaultValue={form.dateTimeEnd}/>
              <Form.Input fluid name = 'topics'  placeholder='Topics' onChange={onChange} defaultValue={form.topics}/>
                <Button color='teal' fluid size='large' type="submit">
                  Create new conference
                </Button>
            </Segment>
          </Form>
          </Grid.Column>
        </Grid>
      </div>
    
    );
}