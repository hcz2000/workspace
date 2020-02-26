package com.jams.authentication.security;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import com.jams.authentication.model.Client;
import com.jams.authentication.repository.ClientRepository;


@Service
public class JPAClientDetailsService implements ClientDetailsService {
	@Autowired
	private ClientRepository clientRepository;
	
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		Client client=clientRepository.findById(clientId);
		if (client == null)
			throw new ClientRegistrationException("Client not found:" + clientId);
		else {

			Set<String> scopeSet=new java.util.HashSet<String>();
			if(client.getScope()!=null) {
				String[] scopes=client.getScope().split("\\|");
				for(String scope:scopes) {
					scopeSet.add(scope);
				}
			}

			Set<String> grantTypeSet=new java.util.HashSet<String>();
			Collection<GrantedAuthority> authorityList=new java.util.ArrayList<GrantedAuthority>();
			if(client.getGrantTypes()!=null) {
				String[] grantTypes=client.getGrantTypes().split("\\|");
				for(String type:grantTypes) {
					grantTypeSet.add(type);
					SimpleAuthority authority=new SimpleAuthority(type);
					authorityList.add(authority);
				}
			}
			
			Set<String> uriSet = new java.util.HashSet<String>();
			if(client.getRedirectUri()!=null) {
				String[] uris = client.getRedirectUri().split("\\|");
				for (String uri : uris) {
					uriSet.add(uri);
				}
			}
			
			boolean autoApprove=false;
			if("true".equalsIgnoreCase(client.getAutoApprove())) {
				autoApprove=true;
			}
			
			SimpleClientDetails clientDetails = new SimpleClientDetails(client.getId(), client.getSecret(), scopeSet,
					grantTypeSet, authorityList,client.getAccessTokenValidity(),client.getRefreshTokenValidity(),
					uriSet,client.getAdditionalInformation(),autoApprove);
			
			return clientDetails;
		}
	}

	class SimpleClientDetails implements ClientDetails {
		private String clientId;
		private String clientSecret;
		private Set<String> scope;
		private Set<String> grantTypes;
		private Collection<GrantedAuthority> authorities;
		private int accessTokenValidity;
		private int refreshTokenValidity;
		private Set<String> redirectUri;
		private String additionalInformation;
		private boolean isAutoApprove;

		public SimpleClientDetails(String clientId, String clientSecret, Set<String> scope, Set<String> grantTypes,
				Collection<GrantedAuthority> authorities,int accessTokenValidity,int refreshTokenValidity,
				Set<String> redirectUri,String additionalInformation,boolean autoApprove) {

			super();
			this.clientId = clientId;
			this.clientSecret = clientSecret;
			this.scope = scope;
			this.grantTypes = grantTypes;
			this.authorities = authorities;
			this.accessTokenValidity=accessTokenValidity;
			this.refreshTokenValidity=refreshTokenValidity;
			this.redirectUri=redirectUri;
			this.additionalInformation=additionalInformation;
			this.isAutoApprove=autoApprove;
		}

		@Override
		public String getClientId() {
			return this.clientId;
		}

		@Override
		public Set<String> getResourceIds() {
			return null;
		}

		@Override
		public boolean isSecretRequired() {
			return true;
		}

		@Override
		public String getClientSecret() {
			return this.clientSecret;
		}

		@Override
		public boolean isScoped() {
			return true;
		}

		@Override
		public Set<String> getScope() {
			return this.scope;
		}

		@Override
		public Set<String> getAuthorizedGrantTypes() {
			return this.grantTypes;
		}

		@Override
		public Set<String> getRegisteredRedirectUri() {
			return this.redirectUri;
		}

		@Override
		public Collection<GrantedAuthority> getAuthorities() {
			return this.authorities;
		}

		@Override
		public Integer getAccessTokenValiditySeconds() {
			return this.accessTokenValidity;
		}

		@Override
		public Integer getRefreshTokenValiditySeconds() {
			return this.refreshTokenValidity;
		}

		@Override
		public boolean isAutoApprove(String scope) {
			return this.isAutoApprove;
		}

		@Override
		public Map<String, Object> getAdditionalInformation() {
			return null;
		}

	}

	class SimpleAuthority implements GrantedAuthority {
		private String authority;
		public SimpleAuthority(String authority) {
			this.authority=authority;
		}
		public String getAuthority() {
			return authority;
		};
	}
}