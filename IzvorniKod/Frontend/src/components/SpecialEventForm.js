import React from 'react';
import { Button, Form, Grid, Header, Segment } from 'semantic-ui-react';
import { MdEvent } from "react-icons/md";
import { Link} from 'react-router-dom';
import { useHistory } from 'react-router-dom';
import axios from 'axios';


export default function Login() {
  const [form, setForm] = React.useState({capacity:'', type:'', message:''});

  const [capacity, setCapacity] = React.useState('');
  const [type, setType] = React.useState("");
  const [message, setMessage] = React.useState("");

  function onChange(event) {
    const {name, value} = event.target;
    setForm(oldForm => ({...oldForm, [name]: value}))
   }

   const user = JSON.parse(localStorage.getItem('user'));
   const token = JSON.parse(localStorage.getItem('token'));

   const history = useHistory(); 

    const routeChange = function() {
      history.push('/specialEvents');
    }

  async function onSubmit(e) {
    e.preventDefault();
    const data = {
         capacity: form.capacity,
         type: form.type,
         message: form.message,
     }
    
     const pConfig = {
      headers: { Authorization: `Bearer ${token.token}`, 'Content-Type': 'application/json'}
    };
     axios.post( 
      `/api/specialEvents/add/${user.conferenceId}`,
       JSON.stringify(data),
       pConfig,
     ).then( function(reponse) {
      alert('The event was succesfully created')
      routeChange();
    })
     .catch( function(error) {
      alert('Something went wrong. Please try again.')
     });
     
  }

 return (
    <div>
    <div style={{width: "250px", margin: "10px"}}>
      <Link to='/myConference'>
        <Button color='teal' fluid size='large'>
          MY CONFERENCE
        </Button>
      </Link>              
    </div>
    <Grid textAlign='center' style={{ height: '100vh' }} verticalAlign='middle'>
    <Grid.Column style={{ maxWidth: 450 }}>
      <Header as='h2' color='teal' textAlign='center'>
        <div>
        <MdEvent style={{ fontSize: '70px'}}/>
        </div>
        New special event
      </Header>
      <Form size='large' onSubmit={onSubmit}>
        <Segment stacked>
          <Form.Input fluid name = 'capacity' placeholder='Capacity'  onChange={onChange} defaultValue={form.capacity} required/>
          <Form.Input
            fluid
            name = 'type'
            placeholder='Type'
            onChange={onChange} defaultValue={form.type}
            required
          />
           <Form.Input
            fluid
            name = 'message'
            placeholder='Message'
            onChange={onChange} defaultValue={form.message}
          />
          <Button color='teal' fluid size='large' type="submit">
            CREATE
          </Button>
        </Segment>
      </Form>
      </Grid.Column>
    </Grid>
  </div>
  );
} 