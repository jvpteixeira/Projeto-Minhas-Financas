import React from 'react'
import Card from '../components/card'
import FormGroup from '../components/form-group'
class login extends React.Component{
  
  state = {
      email : '',
      senha: ''
  }
  entrar = () => {
    console.log('email:', this.state.email)
    console.log('senha:', this.state.senha)
  }

  sair = () => {
    console.log('email:', this.state.email)
    console.log('senha:', this.state.senha)
  }
  render(){
    return (
      <div className="container">
        <div className="row">  
          <div className="col-md-6" style={{position : 'relative', left: '300px'}}>
            <div className="bs-docs-section">
              <Card title="Login">
                  <div className="row">
                    <div className="col-lg-12">
                      <div className="bs-component">
                        <div className="form-group">
                          <FormGroup label="Email: *" htmlFor="exampleInputEmail1">
                          <input type="email" 
                                  className="form-control" 
                                  value={this.state.email}
                                  onChange={e => this.setState({email:e.target.value})}
                                  id="exampleInputEmail1" 
                                  aria-describedby="emailHelp" 
                                  placeholder="Digite o Email"/>
                          </FormGroup>
                          
                          <FormGroup label="Senha: *" htmlFor="exampleInputPassword1">
                          <input type="password" 
                                  className="form-control" 
                                  value={this.state.senha}
                                  onChange={e => this.setState({senha:e.target.value})}
                                  id="exampleInputPassword1" 
                                  placeholder="Password"/>
                          </FormGroup>

                          <button onClick={this.entrar} type="button" className="btn btn-success">Entrar</button>
                          <button onClick={this.sair} type="button" className="btn btn-danger">Cadastrar</button>
                        </div>
                      </div>
                    </div>
                  </div>
              </Card>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

export default login
