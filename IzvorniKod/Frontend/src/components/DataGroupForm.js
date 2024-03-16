import React from 'react';
import { Button, Form, Grid, Header, Segment } from 'semantic-ui-react';
import { BsClipboardData} from "react-icons/bs";
import { Link} from 'react-router-dom';
import { useHistory } from 'react-router-dom';
import axios from 'axios';


export default function DataGroupForm() {

    const [conference, setConference] = React.useState([]);
    
    var user = JSON.parse(localStorage.getItem('user'));
    const token = JSON.parse(localStorage.getItem('token'));

    const config= {
        headers: { Authorization : `Bearer ${token.token}`}
    }

    React.useEffect(() => {
        axios.get(`/api/myConference/${user.idUserAccount}`,
                    config)
        .then(reponse =>  reponse.data)
        .then(conference => setConference(conference))
    }, []);

    const [form, setForm] = React.useState({name:'', data:''});
   
    function onChange(event) {
     const {name, value} = event.target;
     setForm(oldForm => ({...oldForm, [name]: value}))
   }

   //const user = JSON.parse(localStorage.getItem('user'));

   const history = useHistory(); 

    const routeChange = function() {
      history.push('/myConference');
    }

    const pConfig = {
      headers: { Authorization: `Bearer ${token.token}`, 'Content-Type': 'application/json'}
    };

   function onSubmit(e) {
    e.preventDefault();
   const data = {
        idDataGroup: null,
        groupName: form.name,
        data: form.data
   }
    axios.post( 
      `/api/dataGroup/add/${conference.idConference}`,
       JSON.stringify(data),
      pConfig,
   ).then( function(response) {
    alert('Data group added');
    routeChange();
}).catch( function(error) {
        alert('Something went wrong. Please check all the neccesary information');
  });;

    routeChange();
}

return (
    <div>
    <div style={{width: "250px", margin: "10px"}}>
      <Link to='../myConference'>
        <Button color='teal' fluid size='large'>
          Go Back To My Page
        </Button>
      </Link>              
    </div>
    <Grid textAlign='center' style={{ height: '100vh' }} verticalAlign='middle'>
    <Grid.Column style={{ maxWidth: 450 }}>
      <Header as='h2' color='teal' textAlign='center'>
        <div>
        <BsClipboardData style={{ fontSize: '70px'}}/>
        </div>
        New data group
      </Header>
      <Form size='large' onSubmit={onSubmit}>
        <Segment stacked>
          <Form.Input fluid name = 'name' placeholder='Name' onChange={onChange} defaultValue={form.name} required/>
          <Form.Input
            fluid
            name = 'data'
            placeholder='Data'
            onChange={onChange}
            defaultValue={form.data}
            required
          />
          <Button color='teal' fluid size='large' type="submit" >
            Create
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