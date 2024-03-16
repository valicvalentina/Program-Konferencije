import React from 'react';

import { Button, Form, Grid, Header, Segment,Dropdown,Select } from 'semantic-ui-react';
import { BsPersonPlusFill} from "react-icons/bs";
import { Link} from 'react-router-dom';
import { useHistory } from 'react-router-dom';
import ConferenceList from './ConferenceList';
import Conference from "./Conference";
import countries from "i18n-iso-countries";
import axios from 'axios'


export default function Register() {
 
  const token = JSON.parse(localStorage.getItem('token'));

  const [form, setForm] = React.useState({username:'', password:'', firstAndLastName: '', email: '', phoneNumber: '', address:'', company: '', participation:'', conferenceID: ''});

  const [country, setCountry] = React.useState('');
  const [kon, setKon] = React.useState('');
  countries.registerLocale(require("i18n-iso-countries/langs/en.json"));

    const countryObj = countries.getNames("en", {select : "official"});

    const countryArr = Object.entries(countryObj).map(([key, value]) => {
        return {
          key : value,
          value : value,
          text: value
        };
    });
   

  const [conferences, setConferences] = React.useState([]);
 conferences.forEach(conference=>{
conference.text=conference.name;
conference.key=conference.idConference;
conference.value=conference.idConference;
conference.content=(
<Conference key={conference.name} conference={conference}/>
) 
 })
 const state = {
  multiple: false,
  search: true,
  value: '',
  options: conferences
 }
 const a = conferences.map(conference => <Conference key={conference.name} conference={conference}/>);
    const config = {
      headers: { Authorization: `Bearer ${token.token}`}
    };

    const user = JSON.parse(localStorage.getItem('user'));

    var route;
    if(user.userAccountRole==="SYSTEMOWNER") route = '/api/noMainAdmin'
    if(user.userAccountRole==="MAINADMIN") route = `/api/myConferenceList/${user.idUserAccount}`;
    if(user.userAccountRole==="OPERATIVEADMIN") route = `/api/myConferenceList/${user.idUserAccount}`;
  
  React.useEffect(() => {
    axios.get( 
    route,
    config
  ).then(response => {setConferences(response.data)}).catch(console.log);
    }, []);

   function onChange(event) {
    const {name, value} = event.target;
    setForm(oldForm => ({...oldForm, [name]: value}))
   }

   function onCountrySelect(e, {value}) {
    setCountry(value)
    console.log('Drzava ', country)
    console.log('Vrijednost ', value)
   }
   function handleChange (e,{value}){
  
    console.log(value)
    setKon(value)}

   const history = useHistory(); 

    const routeChange = function() {
      history.push('/menu');
    }

   function onSubmit(e) {
    e.preventDefault();
    console.log(kon)
   const data = {
        username: form.username,
        password: form.password,
        firstAndLastName: form.firstAndLastName,
        email: form.email,
        phoneNumber: form.phoneNumber,
        address:form.address,
        country:country,
        companyName: form.company,
        detailsOfParticipation: form.participation,
        conferenceID:JSON.stringify(kon)
        
    }

    const pConfig = {
      headers: { Authorization: `Bearer ${token.token}`, 'Content-Type': 'application/json'}
    };

    if(user.userAccountRole==="SYSTEMOWNER")
    
        axios.post( 
         `/api/users/createMainAdmin/${JSON.stringify(kon )}`,
          JSON.stringify(data),
          pConfig,
        ).then( function(response) {
              alert('Succesfully created a main admin account');
              routeChange();
        }).catch( function(error) {
          console.log(data)
              alert('Something went wrong. Please check all the neccesary information');
        });
    else if(user.userAccountRole==="MAINADMIN")
      axios.post( 
       // `/users/createOperativeAdmin/${Object.values(state["value"])}`,
       `/api/users/createOperativeAdmin/${JSON.stringify(kon )}`,
        JSON.stringify(data),
        pConfig,
      ).then( function(response) {
        alert('Succesfully created an operative admin account');
        routeChange();
        }).catch( function(error) {
              alert('Something went wrong. Please check all the neccesary information');
        });
    else if(user.userAccountRole==="OPERATIVEADMIN")
      axios.post( 
        `/api/users/createParticipant/${JSON.stringify(kon )}`,
         JSON.stringify(data),
        pConfig,
      ).then( function(response) {
            alert('Succesfully created a participant account');
            routeChange();
      }).catch( function(error) {
            alert('Something went wrong. Please check all the neccesary information');
      });
        
   }


   /*function isValid() {
        const {username, password, firstAndLastName, email, phoneNumber, address, country, company, participation} = form;
        const pattern = new RegExp(/^(("[\w-\s]+")|([\w-]+(?:\.[\w-]+)*)|("[\w-\s]+")([\w-]+(?:\.[\w-]+)*))(@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][0-9]\.|1[0-9]{2}\.|[0-9]{1,2}\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\]?$)/i);
        return password.length > 7 && username.length > 0 && firstAndLastName.length > 0 && email > 0 && company > 0 && country > 0
        && password.match(/^.*(?=.{8,})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&]).*$/) && username.match(/^[a-zA-Z ]*$/)
        && phoneNumber.match(/^[0-9]{10}$/) && pattern.test(email);
   } */

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
        <BsPersonPlusFill style={{ fontSize: '70px'}}/>
        </div>
        Create new account
      </Header>
      <Form size='large' onSubmit={onSubmit}>
        <Segment stacked>
          <Form.Input fluid name = 'username' icon='user' iconPosition='left' placeholder='Username' onChange={onChange} defaultValue={form.username} />
          <Form.Input
            fluid
            name = 'password'
            icon='lock'
            iconPosition='left'
            placeholder='Password'
            type='password'
            onChange={onChange}
            defaultValue={form.password}
          />
          <Form.Input fluid name = 'firstAndLastName' placeholder='First and Last name' onChange={onChange} defaultValue={form.firstAndLastName} />
          <Form.Input fluid name = 'email' placeholder='e-mail' onChange={onChange} defaultValue={form.email} />
          <Form.Input fluid name = 'phoneNumber' placeholder='Phone number' onChange={onChange} defaultValue={form.phoneNumber} />
          <Form.Input fluid name = 'address' placeholder='Address'  onChange={onChange} defaultValue={form.address}/>
          <Form.Select fluid name = 'country'  placeholder='Country' onChange={onCountrySelect} multiple={false} value={country} options={countryArr}/>
          <Form.Input fluid name = 'company' placeholder='Company name' onChange={onChange} defaultValue={form.company} />
          <Form.Input fluid name = 'participation' placeholder='Participation' onChange={onChange} defaultValue={form.participation} />
          <Form.Select fluid name = 'conference'  placeholder='Select Conference' onChange={handleChange} multiple={false} value={kon} options={conferences}/>

<br></br>

          <Button color='teal' fluid size='large' type="submit" >
            Register
          </Button>
          <br/>
          <div style={{color: "red", textAlign: "left"}}>
          </div>
          <br/>
        </Segment>
      </Form>
      </Grid.Column>
    </Grid>
  </div>
  );
}
