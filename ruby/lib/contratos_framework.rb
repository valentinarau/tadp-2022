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
    check = method(:check_invariants)

    define_method(method_name) do |*args, &block|
      exec = lambda do |&block|
        unless block.nil?
          unless self.instance_eval &block
            raise 'Validation Error'
          end
        end
      end
      exec.call &method_data[:pre]
      res = orig_meth.bind(self).call *args, &block
      exec.call &method_data[:post]
      check.call
      res
    end

  end
end