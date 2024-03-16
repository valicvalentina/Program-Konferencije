import React from 'react'
import { Container, Divider, Grid, Header, Icon } from 'semantic-ui-react'
import { Link } from 'react-router-dom/cjs/react-router-dom.min';
import '../styles/MenuPage.css';


function MenuPage() {
  var user = JSON.parse(localStorage.getItem('user'));
  return (
    
    <div>
        <style>
      {`
      html, body {
        background-color: #DCDCDC;
      }
      p {
        align-content: center;
        border: 2px solid #2a8686;
        border-radius: 20px;
        background-color: #87CEEB;
        color: white;
        font-weight: bold;
        display: flex;
        flex-direction: column;
        justify-content: center;
        min-height: 7em;
        min-width: 20em;
        margin: 20px;
        
        
      }
      p > span {
        text-align: center;
      }

    
    }
    `}
    </style>
    <Header as='h2' inverted textAlign='center' id = 'name'>
        Welcome {user.firstAndLastName}!
    </Header>
  <Grid id = 'menu'>
    <Grid.Row only='tablet computer'>
      <Grid.Column>
      <Link to="/">
        <p>
          <span>HOME</span>
        </p>
      </Link>
      </Grid.Column>
    </Grid.Row>
    {user.userAccountRole === "MAINADMIN" &&
     <Grid.Row only='tablet computer'>
      <Grid.Column>
      <Link to="/register">
        <p>
          <span>NEW OPERATIVE ADMIN</span>
        </p>
      </Link>
      </Grid.Column>
    </Grid.Row>
    }
    {user.userAccountRole === "OPERATIVEADMIN" &&
     <Grid.Row only='tablet computer'>
      <Grid.Column>
      <Link to="/register">
        <p>
          <span>NEW PARTICIPANT</span>
        </p>
      </Link>
      </Grid.Column>
    </Grid.Row>
    }
    {user.userAccountRole === "SYSTEMOWNER" &&
     <Grid.Row only='tablet computer'>
      <Grid.Column>
      <Link to="/register">
        <p>
          <span>NEW MAIN ADMIN</span>
        </p>
      </Link>
      </Grid.Column>
      </Grid.Row>
    }
    {user.userAccountRole === "SYSTEMOWNER" &&
     <Grid.Row only='tablet computer'>
      <Grid.Column>
      <Link to="/createConference">
        <p>
          <span>CREATE CONFERENCE</span>
        </p>
      </Link>
      </Grid.Column>
      </Grid.Row>
    }
    {user.userAccountRole !== 'SYSTEMOWNER' &&
    <Grid.Row only='tablet computer'>
      <Grid.Column>
      <Link to="/myConference">
        <p>
          <span>MY CONFERENCE</span>
        </p>
        </Link>
      </Grid.Column>
    </Grid.Row>
}
  </Grid>
</div>
);
}

export default MenuPage;