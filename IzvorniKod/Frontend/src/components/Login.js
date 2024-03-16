import React from 'react';
import { Button, Form, Grid, Header, Segment } from 'semantic-ui-react';
import { MdOutlineAccountCircle} from "react-icons/md";
import { Link} from 'react-router-dom';
import { useHistory } from 'react-router-dom';
import axios from 'axios';


export default function Login() {

  const [error, setError] = React.useState('');
  const [username, setUsername] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [user, setUser] = React.useState()

  const history = useHistory(); 

  const routeChange = function() {
    history.push('/menu');
  }

  async function onSubmit(e) {
    e.preventDefault();
    setError("");
    const user = {username, password};
    try {
      const response = await axios.post(
        "/api/users/login",
        user
      );
      setUser(response.data);
      localStorage.setItem('user', JSON.stringify(response.data));
      console.log(JSON.parse(localStorage.getItem('user')));
      const token = await axios.post(
        "/api/authenticate",
        user
      );
      localStorage.setItem('token', JSON.stringify(token.data));
      routeChange();
    } catch(error) {
      setError("Login failed");
    }
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
        <MdOutlineAccountCircle style={{ fontSize: '70px'}}/>
        </div>
        Log in to your account
      </Header>
      <Form size='large' onSubmit={onSubmit}>
        <Segment stacked>
          <Form.Input fluid name = 'username' icon='user' iconPosition='left' placeholder='Username'  onChange={({ target }) => setUsername(target.value)} defaultValue={username} />
          <Form.Input
            fluid
            name = 'password'
            icon='lock'
            iconPosition='left'
            placeholder='Password'
            type='password'
            onChange={({ target }) => setPassword(target.value)} defaultValue={password}
          />
          <Button color='teal' fluid size='large' type="submit">
            Login
          </Button>
          <br/>
          <div style={{color: "red", textAlign: "left"}}>
              {error}
          </div>
          <br/>
        </Segment>
      </Form>
      </Grid.Column>
    </Grid>
  </div>
  );
} 




  