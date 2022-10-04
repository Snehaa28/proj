import { Input, Component, OnInit } from '@angular/core';
import { NgbModal, ModalDismissReasons } from '@ng-bootstrap/ng-bootstrap';
import {
  FormBuilder,
  FormGroup,
  Validators,
  FormControl,
} from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Registration } from './Models/User.Models';
import { RegistrationService } from './Services/Registration.Service';
import { AuthenticationService } from './Services/authentication.service';
import { SharedService } from './Services/shared.service';
import { JsonPipe } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [RegistrationService],
})
export class AppComponent implements OnInit {
  closeResult: string;
  registrationForm: FormGroup;
  loginForm: FormGroup;
  registrationInputs: Registration;
  currentUser: string;
  isLoggedIn: boolean = false;
  cartItemCount: number = 0;
  approvalText: string = '';

  @Input()
  public alerts: Array<IAlert> = [];

  message = '';
  public globalResponse: any;

  constructor(
    private sharedService: SharedService,
    private modalService: NgbModal,
    private fb: FormBuilder,
    private regService: RegistrationService,
    private authService: AuthenticationService
  ) {}
  ngOnInit() {
    this.sharedService.currentMessage.subscribe(
      (msg) => (this.cartItemCount = msg)
    );
    this.registrationForm = this.fb.group({
      customerName: [
        '',
        Validators.compose([
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(50),
        ]),
      ],
      userName: [
        '',
        Validators.compose([
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(50),
        ]),
      ],
      password: [
        '',
        Validators.compose([
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(50),
        ]),
      ],
      address: ['', Validators.required],
      state: ['', Validators.required],
      country: ['', Validators.required],
      zipCode: ['', Validators.required],
      email: ['', Validators.compose([Validators.required, Validators.email])],
      phone: ['', Validators.required],
      altPhone: ['', Validators.required],
    });
    this.loginForm = this.fb.group({
      userName: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  open(content) {
    this.alerts = [];
    this.modalService
      .open(content, { ariaLabelledBy: 'modal-basic-title', size: 'lg' })
      .result.then(
        (result) => {
          this.closeResult = `Closed with: ${result}`;
        },
        (reason) => {
          //this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
        }
      );
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on a backdrop';
    } else {
      return `with: ${reason}`;
    }
  }
  Login() {
    let user = this.loginForm.value;
    this.isLoggedIn = false;
    this.authService.removeToken();
    this.alerts = [];
    //console.log(user);
    this.authService.ValidateUser(user).subscribe(
      (result: { token: string; userName: string }) => {
        this.globalResponse = result;
      },
      (error) => {
        //This is error part
        console.log(error.message);
        this.alerts.push({
          id: 2,
          type: 'danger',
          message: 'Either user name or password is incorrect.',
        });
      },
      () => {
        //  This is Success part
        console.log(this.globalResponse);
        this.authService.storeToken(this.globalResponse.jwttoken);
        this.alerts.push({
          id: 1,
          type: 'success',
          message: 'Login successful. Now you can close and proceed further.',
        });
        this.isLoggedIn = true;
        this.currentUser = this.globalResponse.userName;
        SharedService.currentUser = this.currentUser;
        this.authService.storeRole(this.currentUser);
      }
    );
  }

  OnRegister(registrationForm: FormGroup) {
    this.registrationInputs = this.registrationForm.value;

    console.log(this.registrationInputs);
    this.regService.RegisterUser(this.registrationInputs).subscribe(
      (result: { customerNumber: string }) => {
        this.globalResponse = result.customerNumber;
      },
      (error) => {
        //This is error part
        console.log(error);
        this.alerts.push({
          id: 2,
          type: 'danger',
          message: 'Registration failed with fallowing error:' + error,
        });
      },
      () => {
        //  This is Success part
        this.alerts.push({
          id: 1,
          type: 'success',
          message:
            'Registration successful.CustomerNumber is ' + this.globalResponse,
        });
      }
    );
  }
  public closeAlert(alert: IAlert) {
    const index: number = this.alerts.indexOf(alert);
    this.alerts.splice(index, 1);
  }
  GetClaims() {
    this.authService.getClaims().subscribe(
      (result) => {
        this.globalResponse = result;
      },
      (error) => {
        //This is error part
        console.log(error.message);
      },
      () => {
        //  This is Success part
        // console.log(this.globalResponse );
        let a = this.globalResponse;
        this.currentUser = this.globalResponse.userName;
        this.authService.storeRole(this.currentUser);
      }
    );
  }
  LogOut() {
    this.isLoggedIn = false;
    this.authService.removeToken();
  }
}
export interface IAlert {
  id: number;
  type: string;
  message: string;
}
