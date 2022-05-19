require_relative 'contrato'

class Module
  include Contrato

  def method_added(method_name)
    if should_skip(method_name)
      return
    end

    init_if_needed

    if @wrapped_methods.include?(method_name) # el metodo ya fue wrappeado y se está redefiniendo (vuelve a entrar en method_added para wrappearse)
      @wrapped_methods.delete(method_name)
      return
    end

    @wrapped_methods << method_name
    method_data = extract_method_data
    orig_meth = instance_method(method_name)
    define_method(method_name) do |*args, &block|
      context = Context.new(self, orig_meth.parameters, args) ## TODO que pasa cuando el `orig_meth` es uno ya overrideado ?? tiene params de más
      method_data[:before].each do |block|
        Class.instance_exec(ValidationError, &block)
      end
      context.execute(PreBlockValidationError, &method_data[:pre])
      res = orig_meth.bind(self).call *args, &block
      context.execute(PostBlockValidationError, &method_data[:post])
      method_data[:after].each do |block|
        Class.instance_exec(ValidationError, &block)
      end
      self.class.check_invariants(self)
      res
    end
  end
end