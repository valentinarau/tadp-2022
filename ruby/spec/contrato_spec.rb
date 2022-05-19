describe Contrato do

  describe 'Global before and after conditions' do
    let(:persona) { Persona.new }

    MiClase = Class.new do
      before_and_after_each_call(
        proc{ puts "Entré a un mensaje" },
        proc{ puts "Salí de un mensaje" }
      )
    
      def mensaje_1
        puts "mensaje_1"
        return 5
      end
    
      def mensaje_2
        puts "mensaje_2"
        return 3
      end
    end

    it 'should execute pre, mensaje_2 and post' do
      expected =
        "Entré a un mensaje\n" +
        "mensaje_2\n" +
        "Salí de un mensaje\n"
      expect{ MiClase.new.mensaje_2 }.to output(expected).to_stdout
    end
  end

  describe 'Methods pre and post validations' do
    pre_executed = false
    post_executed = false

    GenericClass = Class.new do
      attr_accessor :int_value
      def initialize
        @int_value = 0
      end

      pre { pre_executed = true }
      post { post_executed = true }
      def method_both_blocks
      end

      pre { pre_executed = true }
      def method_only_pre_block
      end

      post { post_executed = true }
      def method_only_post_block
      end

      def method_none_blocks
      end

      pre { 0 > 1 }
      post { post_executed = true }
      def method_pre_fail_post_pass
      end

      pre { pre_executed = true }
      post { 0 > 1 }
      def method_pre_pass_post_fail
      end

      pre { 0 > 1 }
      post { 0 > 1 }
      def method_pre_fail_post_fail
      end

      pre { pre_executed = true }
      post { post_executed = true }
      def method_pre_pass_post_pass
        @int_value = 2
      end

      pre { set_algo true }
      def get_algo
        @algo
      end

      def set_algo(val)
        @algo = val
      end
    end

    let(:generic_class) { GenericClass.new }
    before(:each) do
      pre_executed = false
      post_executed = false
    end

    describe "Execute only for it's method" do
      it 'should execute pre and post blocks for method_both_blocks' do
        generic_class.method_both_blocks
        expect(pre_executed).to be true
        expect(post_executed).to be true
      end

      it 'should execute only pre block for method_only_pre_block' do
        generic_class.method_only_pre_block
        expect(pre_executed).to be true
        expect(post_executed).to be false
      end

      it 'should execute only post block for method_only_post_block' do
        generic_class.method_only_post_block
        expect(pre_executed).to be false
        expect(post_executed).to be true
      end

      it 'should no block be executed for method_none_blocks' do
        generic_class.method_none_blocks
        expect(pre_executed).to be false
        expect(post_executed).to be false
      end
    end

    describe 'Exceptions' do
      it 'should raise an exception when any block does not pass' do
        expect { generic_class.method_pre_fail_post_pass }.to raise_error(PreBlockValidationError)
        expect { generic_class.method_pre_pass_post_fail }.to raise_error(PostBlockValidationError)
        expect { generic_class.method_pre_fail_post_fail }.to raise_error(PreBlockValidationError)
      end

      it 'should pass and return value' do
        expect{ generic_class.method_pre_pass_post_pass }.not_to raise_error
        int_value = generic_class.int_value
        expect(pre_executed).to be true
        expect(post_executed).to be true
        expect(int_value).to eq 2
      end
    end

    it 'should can call instance methods from PRE/POST blocks' do
      expect(generic_class.get_algo).to be true
    end
  end

  describe 'Invariants' do
    NoInvariantsClass = Class.new do
      def generic_method
      end
    end

    SingleInvariantClass = Class.new do
      def initialize(should_pass)
        @bool_var = should_pass
      end

      invariant { @bool_var }
      def generic_method
      end
    end


    MultipleInvariantsClass = Class.new do
      def initialize
        @numeric_var = 10
        @str_var = 'Hi!'
        @im_an_array = []
      end

      invariant { @numeric_var = 10 }
      invariant { @im_an_array.empty? }
      invariant { !@str_var.nil? }

      def generic_method
      end
    end

    let(:no_invariants_class) { NoInvariantsClass.new }
    let(:single_invariant_class) { SingleInvariantClass.new true }
    let(:single_invariant_error_class) { SingleInvariantClass.new false }
    let(:multiple_invariants_class) { MultipleInvariantsClass.new }

    it 'should not raise an error for no invariants class' do
      expect { no_invariants_class.generic_method }.not_to raise_error
    end

    it 'should raise an error for single invariant with error class' do
      expect { single_invariant_error_class.generic_method }.to raise_error(InvariantError)
    end

    it 'should not raise an error for single (correct) invariant class' do
      expect { single_invariant_class.generic_method }.not_to raise_error
    end

    it 'should not raise an error for multiple (correct) invariant class' do
      expect { multiple_invariants_class.generic_method }.not_to raise_error
    end

    it 'should raise error with an invalid invariant' do
      multiple_invariants_class.instance_variable_set(:@im_an_array, [1,2,3])
      expect { multiple_invariants_class.generic_method }.to raise_error(InvariantError)
    end

  end

end

