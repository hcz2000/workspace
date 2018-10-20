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
			String[] scopes=client.getScope().split("\\|");
			Set<String> scopeSet=new java.util.HashSet<String>();
			for(String scope:scopes) {
				scopeSet.add(scope);
			}

			String[] grantTypes=client.getGrantTypes().split("\\|");
			Set<String> grantTypeSet=new java.util.HashSet<String>();
			Collection<GrantedAuthority> authorityList=new java.util.ArrayList<GrantedAuthority>();
			for(String type:grantTypes) {
				grantTypeSet.add(type);
				SimpleAuthority authority=new SimpleAuthority(type);
				authorityList.add(authority);
			}
			
			SimpleClientDetails clientDetails=new SimpleClientDetails(client.getId(),client.getSecret(),scopeSet,grantTypeSet,authorityList);
			
			return clientDetails;
		}
	}

	class SimpleClientDetails implements ClientDetails {
		private String clientId;
		private String clientSecret;
		private Set<String> scope;
		private Set<String> grantTypes;
		private Collection<GrantedAuthority> authorities;
		
		public SimpleClientDetails(String clientId, String clientSecret, Set<String>scope, Set<String>grantTypes,Collection<GrantedAuthority>authorities) {
			super();
			this.clientId = clientId;
			this.clientSecret = clientSecret;
			this.scope = scope;
			this.grantTypes=grantTypes;
			this.authorities=authorities;
		}

		@Override
		public String getClientId() {
			return clientId;
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
			return clientSecret;
		}

		@Override
		public boolean isScoped() {
			return true;
		}

		@Override
		public Set<String> getScope() {
			return scope;
		}

		@Override
		public Set<String> getAuthorizedGrantTypes() {
			return grantTypes;
		}

		@Override
		public Set<String> getRegisteredRedirectUri() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Collection<GrantedAuthority> getAuthorities() {
			return authorities;
		}

		@Override
		public Integer getAccessTokenValiditySeconds() {
			return 3600;
		}

		@Override
		public Integer getRefreshTokenValiditySeconds() {
			return 3600;
		}

		@Override
		public boolean isAutoApprove(String scope) {
			return true;
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
